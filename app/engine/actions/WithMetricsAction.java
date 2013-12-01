package engine.actions;

import com.codahale.metrics.Timer;
import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

import java.util.Map;

import static engine.metrics.Metrics.WootStarMetrics;

public class WithMetricsAction extends Action<WithMetrics>
{
    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable
    {
        final Timer.Context context = WootStarMetrics().getAllRequestsTimer().time();
        try
        {
            Logger.info(ctx.request().toString());
            Map<String, String[]> headers = ctx.request().headers();
            for(String k : headers.keySet())
            {
                Logger.info("Header " + k);
                for(String v : headers.get(k))
                {
                    Logger.info("Value " + v);
                }
            }
            return delegate.call(ctx);
        } finally
        {
            context.stop();
        }
    }
}