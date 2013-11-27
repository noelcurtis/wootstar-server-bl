package controllers;

import play.mvc.Result;

public class Application extends ControllerEx
{
    public static Result index()
    {
        return ok(views.html.application.index.render());
    }
}
