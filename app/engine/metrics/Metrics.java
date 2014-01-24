package engine.metrics;

import akka.actor.Cancellable;
import com.codahale.metrics.*;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Metrics
{

    private final static MetricRegistry metricsRegistry = new MetricRegistry();  // The REGISTERY

    private final static MemoryUsageGaugeSet memoryUsageGaugeSet = new MemoryUsageGaugeSet(); // Memory Guage Set for the JVM
    private static Timer allRequestsTimer; // Timer used to time Controller actions see @WithMetrics
    private final Map<String, Timer> otherTimers; // Timers for Woot Getters
    private final CacheHitRatio cacheHitRatioGuage;

    // Console Reporter //
    private static ConsoleReporter consoleReporter;
    private static boolean consoleReportedEnabled = false;
    // Console Reporter //

    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    private static Cancellable scheduledMetrics; // scheduled metrics cancellable

    private static Metrics _sharedInstance = null; // shared instance for Singleton

    public static Metrics WootStarMetrics()
    {
        if (_sharedInstance == null)
        {
            Logger.info("Initiating metrics");
            _sharedInstance = new Metrics();
        }
        return _sharedInstance;
    }

    protected Metrics()
    {
        if (consoleReportedEnabled)
        {
            consoleReporter = ConsoleReporter.forRegistry(metricsRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build();
            consoleReporter.start(15, TimeUnit.MINUTES);
        }

        otherTimers = new HashMap<String, Timer>();
        cacheHitRatioGuage = new CacheHitRatio(metricsRegistry.counter(MetricRegistry.name("hits", "counter")), metricsRegistry.counter(MetricRegistry.name("calls", "counter")));
        metricsRegistry.register(MetricRegistry.name("application", "cache-hits"), cacheHitRatioGuage);
        registerMemoryMetrics();
        registerTimers();
    }

    private void registerMemoryMetrics()
    {
        metricsRegistry.register(MetricRegistry.name("application", "memory-usage"), new MetricSet()
        {
            @Override
            public Map<String, Metric> getMetrics()
            {
                return memoryUsageGaugeSet.getMetrics();
            }
        });
    }

    private void registerTimers()
    {
        allRequestsTimer = metricsRegistry.timer(MetricRegistry.name("allrequests", "timer"));
    }


    public void startScheduledMetrics()
    {
        int offset = 10;
        Logger.info(String.format("Starting Woot DataGetter in %d seconds...", offset));
        if (scheduledMetrics != null && !scheduledMetrics.isCancelled())
        {
            Logger.info("May already be started ... ");
            return;
        }
        scheduledMetrics = Akka.system().scheduler().schedule(Duration.create(offset, TimeUnit.SECONDS),
                Duration.create(10, TimeUnit.MINUTES), new Runnable()
        {
            @Override
            public void run()
            {

            }
        }, Akka.system().dispatcher());
    }

    public void cancelScheduledMetrics()
    {
        if (scheduledMetrics != null)
        {
            scheduledMetrics.cancel();
        }
    }

    public ObjectMapper getMapper()
    {
        return mapper;
    }

    public MetricRegistry getMetricsRegistry()
    {
        return metricsRegistry;
    }

    public Timer getAllRequestsTimer()
    {
        return allRequestsTimer;
    }

    public Timer getTimer(String timerName)
    {
        Timer timer = otherTimers.get(timerName);
        if (timer != null)
        {
            return  timer;
        }
        else
        {
            timer = metricsRegistry.timer(MetricRegistry.name("otherTimer", timerName));
            otherTimers.put(timerName, timer);
        }
        return timer;
    }

    public CacheHitRatio getCacheHitRatioGuage()
    {
        return cacheHitRatioGuage;
    }

}
