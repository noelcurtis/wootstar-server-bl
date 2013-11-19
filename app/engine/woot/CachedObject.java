package engine.woot;

import com.fasterxml.jackson.databind.JsonNode;
import models.Event;

import java.util.ArrayList;
import java.util.List;

import static engine.WootObjectMapper.WootMapper;

public class CachedObject
{
    private final JsonNode json;
    private final engine.data.apiv1.Event[] events;

    public CachedObject(engine.data.apiv1.Event event, JsonNode json)
    {
        this.events = new engine.data.apiv1.Event[]{event};
        this.json = json;
    }

    public CachedObject(List<engine.data.apiv1.Event> events, JsonNode json)
    {
        this.events = events.toArray(new engine.data.apiv1.Event[]{});
        this.json = json;
    }

    public engine.data.apiv1.Event[] getEvents()
    {
        return events;
    }

    public JsonNode getJson()
    {
        return json;
    }
}
