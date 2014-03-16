package engine.woot;

import akka.actor.Cancellable;
import engine.JedisManager;
import engine.Utils;
import engine.metrics.ActiveUsersMonitor;
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
    private Cancellable cleanActiveUsers;
    private boolean isDataGetter = false;

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
//        JedisManager.SharedJedisManager().flush();
        isDataGetter = true;
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
        boolean done = true;
        for (Cancellable c : activeRequests)
        {
            done = c.cancel();
        }
        if (done)
        {
            activeRequests = new ArrayList<Cancellable>();
        }
        Logger.error("Error cancelling requests!");
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
        return new ArrayList<WootRequest>(requests);
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
                    for (int i = 0; i < 60; i++)
                    {
                        WootGetterZ g = new WootGetterZ(new WootRequest(WootApiHelpers.EventType.Daily, null));
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

    public void scheduleDataGetterCheck()
    {
        Cancellable c = Akka.system().scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS),
                Duration.create(0, TimeUnit.MILLISECONDS), new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // If you are not the datagetter, try and become the data getter
                    if(!isDataGetter)
                    {
                        if (!Utils.eventsAreBeingUpdated() && JedisManager.SharedJedisManager().iCanBeDataGetter())
                        {
                            // Schedule all the requests
                            RequestQueue().scheduleRequests();
                            // schedule a restart
                            RequestQueue().scheduleDailyRefresh();
                            // Schedule clear active users
                            RequestQueue().scheduleClearActiveUsers();
                        }
                    }
                }
                catch (Exception ex)
                {
                    Logger.error("Error scheduling data getter check " + ex.toString());
                    ex.printStackTrace();
                }

            }
        }, Akka.system().dispatcher());
    }
}
