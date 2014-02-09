package engine.woot;

import akka.actor.Cancellable;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WootRequestQueue
{
    private static WootRequestQueue sharedInstance;

    private final List<WootRequest> requests;
    private List<Cancellable> activeRequests;

    public static WootRequestQueue RequestQueue()
    {
        if (sharedInstance == null)
        {
            sharedInstance = new WootRequestQueue();
        }
        return sharedInstance;
    }

    private WootRequestQueue()
    {
        activeRequests = new ArrayList<Cancellable>();
        requests = new ArrayList<WootRequest>();

        requests.add(new WootRequest(WootApiHelpers.EventType.Daily, null)); // 10 min refresh cycle
        requests.add(new WootRequest(7000l, WootApiHelpers.EventType.WootOff, null)); // 7 second refresh cycle
        requests.add(new WootRequest(3600000l, WootApiHelpers.EventType.Moofi, null)); // 1 hour refresh cycle
        requests.add(new WootRequest(3600000l, WootApiHelpers.EventType.Reckoning, null)); // 1 hour refresh cycle

        // woot plus all sites individually
        for (WootApiHelpers.Site s : WootApiHelpers.Site.values())
        {
            requests.add(new WootRequest(WootApiHelpers.EventType.WootPlus, s)); // 10 min refresh cycle
        }

        // .. add some more requests here !!
    }

    public void scheduleRequests()
    {
        //JedisManager.SharedJedisManager().flush();
        int t = 0;
        for (final WootRequest r : requests)
        {
            Logger.info("Scheduling request " + r.toString());
            Cancellable c = Akka.system().scheduler().schedule(
                    Duration.create(t, TimeUnit.SECONDS),
                    Duration.create(r.interval, TimeUnit.MILLISECONDS), new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        WootGetterZ g = new WootGetterZ(r);
                        g.getEvents(); // get events
                    }
                    catch (Exception ex)
                    {
                        Logger.error("Error scheduling requests " + ex.toString());
                        ex.printStackTrace();
                    }

                }
            }, Akka.system().dispatcher());
            activeRequests.add(c);
            if (Play.isProd())
            {
                t +=30; // increment for 30 sec offset so updates are distributed in production.
            }
        }
        // schedule a restart
        //RequestQueue().scheduleRestart();
    }

    public void cancelRequests()
    {
        Logger.info("Cancelling requests");
        for (Cancellable c : activeRequests)
        {
            c.cancel();
        }
        activeRequests = new ArrayList<Cancellable>();
    }

    public List<WootRequest> getRequests()
    {
        return requests;
    }

    public void scheduleRestart()
    {
        // note that time is UTC on EC-2 instances
        final DateTime time = new DateTime();
        org.joda.time.Duration duration = new org.joda.time.Duration(time, time.plusDays(1).toDateMidnight());
        // Add 5 hours for UTC + 1 hour for 1:00am
        duration = duration.plus(3600000*6);
        Logger.info("Scheduling a restart after " + duration.getStandardSeconds() + " seconds");
        Cancellable c = Akka.system().scheduler().schedule(
                Duration.create(duration.getStandardSeconds(), TimeUnit.SECONDS),
                Duration.create(24, TimeUnit.HOURS), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // cancel all the requests
                    cancelRequests();
                    // restart all the requests
                    scheduleRequests();
                }
                catch (Exception ex)
                {
                    Logger.error("Error restarting requests " + ex.toString());
                    ex.printStackTrace();
                }
            }
        }, Akka.system().dispatcher());
    }
}
