package engine;

import play.Application;
import play.GlobalSettings;
import play.Logger;

import static engine.metrics.Metrics.WootStarMetrics;
import static engine.woot.WootRequestQueue.RequestQueue;

public class Global extends GlobalSettings
{
    @Override
    public void onStart(Application app)
    {
        Logger.info("Application startup...");
        if (app.configuration().getBoolean("datagetter.enabled"))
        {
            try
            {
                WootStarMetrics();
                RequestQueue().scheduleRequests();
            }
            catch(Exception ex)
            {
                Logger.error(ex.toString());
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onStop(Application app)
    {
        Logger.info("Application shutdown...");
        RequestQueue().cancelRequests();
        JedisManager.SharedJedisManager().getPool().destroy();
    }
}
