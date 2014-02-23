package controllers;

import engine.actions.Secured;
import engine.actions.WithSsl;
import engine.metrics.ActiveUsersMonitor;
import play.mvc.Result;

public class Metrics extends ControllerEx
{
    @WithSsl
    @Secured
    public static Result startSession()
    {
        ActiveUsersMonitor.startSession(request());
        response().setContentType("application/json");
        return ok();
    }

    @WithSsl
    @Secured
    public static Result endSession()
    {
        ActiveUsersMonitor.endSession(request());
        response().setContentType("application/json");
        return ok();
    }
}
