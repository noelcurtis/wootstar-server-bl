package controllers;

import com.google.common.base.Objects;
import engine.actions.WithSsl;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.util.Map;

public class Test extends Controller
{
    @WithSsl
    public static Result events()
    {
        response().setContentType("application/json");
        Map<String, String[]> requestParams = request().queryString();
        if (requestParams.containsKey("id"))
        {
            String id = Objects.firstNonNull(requestParams.get("id")[0], "");
            return byId(id);
        } else if (requestParams.containsKey("eventType"))
        {
            String type = Objects.firstNonNull(requestParams.get("eventType")[0], "");
            return byType(type);
        } else
        {
            return all();
        }
    }

    @WithSsl
    private static Result all()
    {
        File f = new File("app/views/test/events_no_wootoff.json");
        return ok(f, true);
    }

    @WithSsl
    private static Result byId(String id)
    {
        return ok("");
    }

    @WithSsl
    private static Result byType(String type)
    {
        if (type.equals("WootOff"))
        {
            File f = new File("app/views/test/wootoff.json");
            return ok(f, true);
        }
        return ok("");
    }
}
