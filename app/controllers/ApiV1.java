package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import engine.actions.Secured;
import engine.actions.WithMetrics;
import engine.actions.WithSsl;
import engine.data.apiv1.Settings;
import engine.woot.WootReponseBuilder;
import engine.woot.responses.AllEventsBuilder;
import engine.woot.responses.EventIdBuilder;
import engine.woot.responses.EventTypeBuilder;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Map;

public class ApiV1 extends ControllerEx
{

    /**
     * Will return a list of data.apiv1.Settingss
     *
     * @return
     */
    @WithSsl
    @Secured
    public static Result settings()
    {
        return gzippedOk(Settings.getSettings());
    }

    @WithSsl
    @Secured
    public static Result updateSettings()
    {
        Http.Request r = request();
        JsonNode settings = r.body().asJson();
        Settings.setSettings(settings);
        return ok(settings);
    }

    /**
     * Will return a list of data.apiv1.Event if no id is passed in
     * Will return a model.Event if event id is passed in
     * Will return list of model.Event if event type is passed in
     *
     * @return
     */
    @WithSsl
    @Secured
    @WithMetrics
    public static Result events()
    {
        Map<String, String[]> requestParams = request().queryString();
        if (requestParams.containsKey("id"))
        {
            String id = Objects.firstNonNull(requestParams.get("id")[0], "");
            return byId(id);
        } else if (requestParams.containsKey("type"))
        {
            String type = Objects.firstNonNull(requestParams.get("type")[0], "");
            return byType(type);
        } else
        {
            return all();
        }
    }

    private static Result all()
    {
        final WootReponseBuilder b = new AllEventsBuilder();
        if (eTagTest(b.getEtag()))
        {
            return noChange(); // no change in data
        }
        else
        {
            if (b.isError())
            {
                return internalServerError(b.getResponse());
            }
            return gzippedOk(b.getResponse(), b.getEtag());
        }
    }

    private static Result byId(String id)
    {
        final WootReponseBuilder b = new EventIdBuilder(id);
        if (eTagTest(b.getEtag()))
        {
            return noChange(); // no change in data
        }
        else
        {
            if (b.isError())
            {
                return internalServerError(b.getResponse());
            }
            return gzippedOk(b.getResponse(), b.getEtag());
        }
    }

    private static Result byType(String type)
    {
        final WootReponseBuilder b = new EventTypeBuilder(type);
        if (eTagTest(b.getEtag()))
        {
            return noChange(); // no change in data
        }
        else
        {
            if (b.isError())
            {
                return internalServerError(b.getResponse());
            }
            return gzippedOk(b.getResponse(), b.getEtag());
        }
    }
}
