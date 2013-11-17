package controllers;

import com.google.common.base.Objects;
import engine.actions.WithMetrics;
import engine.data.apiv1.Settings;
import engine.woot.WootReponseBuilder;
import engine.woot.responses.AllEventsBuilder;
import engine.woot.responses.EventIdBuilder;
import engine.woot.responses.EventTypeBuilder;
import play.mvc.Result;

import java.util.Map;

public class ApiV1 extends ControllerEx
{

    /**
     * Will return a list of data.apiv1.Settingss
     *
     * @return
     */
    public static Result settings()
    {
        return gzippedOk(Settings.getSettings());
    }

    /**
     * Will return a list of data.apiv1.Event if no id is passed in
     * Will return a model.Event if event id is passed in
     * Will return list of model.Event if event type is passed in
     *
     * @return
     */
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

    @WithMetrics
    private static Result all()
    {
        WootReponseBuilder b = new AllEventsBuilder();
        return gzippedOk(b.getResponse());
    }

    @WithMetrics
    private static Result byId(String id)
    {
        WootReponseBuilder b = new EventIdBuilder(id);
        return ok(b.getResponse());
    }

    @WithMetrics
    private static Result byType(String type)
    {
        WootReponseBuilder b = new EventTypeBuilder(type);
        return ok(b.getResponse());
    }
}
