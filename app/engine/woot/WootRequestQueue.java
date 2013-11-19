package engine.woot;

import akka.actor.Cancellable;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

        requests.add(new WootRequest(WootApiHelpers.EventType.Daily, null)); // 10 min refresh cycle
        requests.add(new WootRequest(40000l, WootApiHelpers.EventType.Moofi, null)); // 1 hour refresh cycle
        requests.add(new WootRequest(40000l, WootApiHelpers.EventType.Reckoning, null)); // 1 hour refresh cycle
        requests.add(new WootRequest(7000l, WootApiHelpers.EventType.WootOff, null)); // 7 second refresh cycle

        // woot plus all sites individually
        for (WootApiHelpers.Site s : WootApiHelpers.Site.values())
        {
            requests.add(new WootRequest(WootApiHelpers.EventType.WootPlus, s)); // 10 min refresh cycle
        }

        // .. add some more requests here !!
    }

    public void scheduleRequests()
    {
        for (final WootRequest r : requests)
        {
            Logger.info("Scheduling request " + r.toString());
            //Random rand = new Random();
           // final int  n = rand.nextInt(10) + 1;
            Cancellable c = Akka.system().scheduler().schedule(
                    Duration.create(0, TimeUnit.SECONDS), // Initial delay between 0-10 seconds
                    Duration.create(r.interval, TimeUnit.MILLISECONDS), new Runnable()
            {
                @Override
                public void run()
                {
                    WootGetter g = new WootGetter(r);
                    g.getEvents(); // get events
                }
            }, Akka.system().dispatcher());
            activeRequests.add(c);
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
