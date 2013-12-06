package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxType;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import engine.DbHelpers;
import engine.actions.WithSsl;
import models.Event;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.util.List;
import java.util.Map;

import static engine.WootObjectMapper.WootMapper;

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
