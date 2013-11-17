package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxType;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import engine.DbHelpers;
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
    public static Result testSetup()
    {
        final long startTime = System.currentTimeMillis();
        WS.url("http://localhost:9000/test/events").get().onRedeem(
                new F.Callback<WS.Response>()
                {
                    @Override
                    @Transactional(type = TxType.REQUIRES_NEW)
                    public void invoke(WS.Response response) throws Throwable
                    {
                        try
                        {
                            ObjectMapper om = new ObjectMapper(); // map response
                            List<Event> events = om.readValue(response.getBody(), WootMapper().getTypeFactory().constructCollectionType(List.class, Event.class));
                            if (!events.isEmpty())
                            {
                                DbHelpers.clearWootData(); // clear the database
                                Logger.info("Saving new events start");
                                Ebean.save(events); //save all the events
                                Logger.info("Saving new events end");
                            }
                        } catch (Exception ex)
                        {
                            Logger.error("Error Refreshing Database: " + ex.toString());
                            Logger.error("Woot Response status " + response.getStatus());
                            Logger.error("Woot Response " + response.toString());
                            ex.printStackTrace();
                        }
                        final long timeTaken = System.currentTimeMillis() - startTime;
                        Logger.info("Getting and saving events took: {" + timeTaken + "}ms");
                    }
                });
        return ok("Run 'select count(*) from event;' in your DB, there should be 101 events. Check the play logs for errors if any!");
    }

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

    private static Result all()
    {
        File f = new File("app/views/test/events_no_wootoff.json");
        return ok(f, true);
    }

    private static Result byId(String id)
    {
        return ok("");
    }

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
