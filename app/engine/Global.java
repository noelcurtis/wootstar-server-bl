package engine;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;

import static engine.metrics.Metrics.WootStarMetrics;
import static engine.woot.WootRequestQueue.RequestQueue;
import static play.mvc.Results.*;

public class Global extends GlobalSettings
{
    @Override
    public void onStart(Application app)
    {
        JedisManager.SharedJedisManager();
        Logger.info("Application startup...");
        WootStarMetrics();
    }

    @Override
    public void onStop(Application app)
    {
        Logger.info("Application shutdown...");
        RequestQueue().cancelRequests();
        RequestQueue().cancelCleanActiveUsers();
        JedisManager.SharedJedisManager().getPool().destroy();
    }

    @Override
    public F.Promise<SimpleResult> onError(Http.RequestHeader request, Throwable t) {
        Logger.error(t.getMessage());
        t.printStackTrace();
        Logger.info(t.toString());
        SimpleResult res = internalServerError(Utils.jsonError(t.getMessage()));
        return F.Promise.pure(res);
    }

    @Override
    public F.Promise<SimpleResult> onHandlerNotFound(Http.RequestHeader request) {
        return F.Promise.pure(redirect("/"));
    }

    @Override
    public F.Promise<SimpleResult> onBadRequest(Http.RequestHeader request, String error) {
        SimpleResult res = badRequest(Utils.jsonError(error));
        return F.Promise.pure(res);
    }
}
