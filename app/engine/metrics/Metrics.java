package engine.metrics;

import akka.actor.Cancellable;
import com.codahale.metrics.*;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Metrics
{

    private final static MetricRegistry metricsRegistry = new MetricRegistry();  // The REGISTERY

    private final static MemoryUsageGaugeSet memoryUsageGaugeSet = new MemoryUsageGaugeSet(); // Memory Guage Set for the JVM
    private static Timer allRequestsTimer; // Timer used to time Controller actions see @WithMetrics

    // Console Reporter //
    private static ConsoleReporter consoleReporter;
    private static boolean consoleReportedEnabled = true;
    // Console Reporter //

    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    private static Cancellable scheduledMetrics; // scheduled metrics cancellable

    private static Metrics _sharedInstance; // shared instance for Singleton

    public static Metrics WootStarMetrics()
    {
        if (_sharedInstance == null)
        {
            _sharedInstance = new Metrics();
        }
        return _sharedInstance;
    }

    public Metrics()
    {
        if (consoleReportedEnabled)
        {
            consoleReporter = ConsoleReporter.forRegistry(metricsRegistry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build();
            consoleReporter.start(1, TimeUnit.MINUTES);
        }

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

}
