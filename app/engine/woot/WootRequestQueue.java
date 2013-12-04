package engine.woot;

import akka.actor.Cancellable;
import engine.JedisManager;
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
    private final List<Cancellable> activeRequests;

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

        requests.add(new WootRequest(7000l, WootApiHelpers.EventType.WootOff, null)); // 7 second refresh cycle
        requests.add(new WootRequest(WootApiHelpers.EventType.Daily, null)); // 10 min refresh cycle
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
                    Duration.create(t, TimeUnit.MINUTES),
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
                t +=4; // increment for 4 minute offset so updates are distributed in production.
            }
        }
    }

    public void cancelRequests()
    {
        for (Cancellable c : activeRequests)
        {
            c.cancel();
            activeRequests.remove(c);
        }
    }

    public List<WootRequest> getRequests()
    {
        return requests;
    }
}
