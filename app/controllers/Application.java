package controllers;

import engine.actions.WithSsl;
import play.mvc.Result;

public class Application extends ControllerEx
{
    @WithSsl
    public static Result index()
    {
        return ok(views.html.application.index.render());
    }
}
