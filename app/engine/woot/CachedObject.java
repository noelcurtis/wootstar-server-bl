package engine.woot;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.List;

public class CachedObject
{
    private final JsonNode json;
    private final engine.data.apiv1.Event[] events;
    private final Date timestamp;


    public CachedObject(engine.data.apiv1.Event event, JsonNode json,
                        WootApiHelpers.EventType eventType, WootApiHelpers.Site site, Date timestamp)
    {
        this.events = new engine.data.apiv1.Event[]{event};
        this.json = json;
        this.timestamp = timestamp;
    }

    public CachedObject(List<engine.data.apiv1.Event> events, JsonNode json, Date timestamp)
    {
        this.events = events.toArray(new engine.data.apiv1.Event[]{});
        this.json = json;
        this.timestamp = timestamp;
    }

    public engine.data.apiv1.Event[] getEvents()
    {
        return events;
    }

    public JsonNode getJson()
    {
        return json;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

}
