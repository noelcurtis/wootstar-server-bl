package controllers;

import engine.Utils;
import play.mvc.Controller;
import play.mvc.Result;

import static engine.metrics.Metrics.WootStarMetrics;

public class Admin extends Controller
{
    public static Result index()
    {
        return ok(views.html.admin.index.render(Utils.getHostName()));
    }

    public static Result metrics()
    {
        try
        {
            return ok(WootStarMetrics().getMapper().valueToTree(WootStarMetrics().getMetricsRegistry()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return internalServerError(Utils.jsonError());
    }
}
