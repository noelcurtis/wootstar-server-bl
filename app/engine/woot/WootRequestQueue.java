package engine.woot;

import akka.actor.Cancellable;
import engine.metrics.ActiveUsersMonitor;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WootRequestQueue
{
    private static WootRequestQueue sharedInstance;

    private final Map<WootApiHelpers.EventType,WootRequest> requests;
    private List<Cancellable> activeRequests;
    private Cancellable cleanActiveUsers;

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
        requests = new HashMap<WootApiHelpers.EventType, WootRequest>();

        requests.put(WootApiHelpers.EventType.Daily, new WootRequest(WootApiHelpers.EventType.Daily, null)); // 10 min refresh cycle
        requests.put(WootApiHelpers.EventType.WootOff, new WootRequest(7000l, WootApiHelpers.EventType.WootOff, null)); // 7 second refresh cycle
        requests.put(WootApiHelpers.EventType.Moofi, new WootRequest(3600000l, WootApiHelpers.EventType.Moofi, null)); // 1 hour refresh cycle
        requests.put(WootApiHelpers.EventType.Reckoning, new WootRequest(3600000l, WootApiHelpers.EventType.Reckoning, null)); // 1 hour refresh cycle

        // woot plus all sites individually
        for (WootApiHelpers.Site s : WootApiHelpers.Site.values())
        {
            requests.put(WootApiHelpers.EventType.WootPlus, new WootRequest(WootApiHelpers.EventType.WootPlus, s)); // 10 min refresh cycle
        }

        // .. add some more requests here !!
    }

    public void scheduleRequests()
    {
        //JedisManager.SharedJedisManager().flush();
        int t = 0;
        for (final WootRequest r : requests.values())
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
    }

    /**
     * Use to schedule clear active users, every 5 hours
     */
    public void scheduleClearActiveUsers()
    {
        cleanActiveUsers = Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.SECONDS),
                Duration.create(5, TimeUnit.HOURS), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ActiveUsersMonitor.cleanActiveUsers();
                }
                catch (Exception ex)
                {
                    Logger.error("Error scheduling requests " + ex.toString());
                    ex.printStackTrace();
                }

            }
        }, Akka.system().dispatcher());
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

    public void cancelCleanActiveUsers()
    {
        if (cleanActiveUsers != null)
        {
            cleanActiveUsers.cancel();
        }
    }

    public List<WootRequest> getRequests()
    {
        return new ArrayList<WootRequest>(requests.values());
    }


    public void scheduleDailyRefresh()
    {
        // note that time is UTC on EC-2 instances
        final DateTime time = new DateTime();
        org.joda.time.Duration duration = new org.joda.time.Duration(time, time.plusDays(1).toDateMidnight());
        // Add 6 hours for UTC for 1:00am
        duration = duration.plus(3600000*6);
        Cancellable c = Akka.system().scheduler().schedule(
                Duration.create(duration.getStandardSeconds(), TimeUnit.SECONDS),
                Duration.create(24, TimeUnit.HOURS), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (int i = 0; i < 10; i++)
                    {
                        WootGetterZ g = new WootGetterZ(requests.get(WootApiHelpers.EventType.Daily));
                        g.getEvents();
                        Thread.sleep(10000L);
                    }
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
