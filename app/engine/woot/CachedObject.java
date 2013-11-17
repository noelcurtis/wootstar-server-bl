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

    public CachedObject(Event event)
    {
        engine.data.apiv1.Event mappedEvent = new engine.data.apiv1.Event(event);
        this.events = new engine.data.apiv1.Event[]{mappedEvent};
        this.json = WootMapper().valueToTree(mappedEvent);
    }

    public CachedObject(List<Event> events)
    {
        List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
        // map events so they can be rendered in json
        for (models.Event e : events)
        {
            mappedEvents.add(new engine.data.apiv1.Event(e));
        }
        // get the events
        this.events = mappedEvents.toArray(new engine.data.apiv1.Event[]{});
        // serialize the JSON
        this.json = WootMapper().valueToTree(this.events);
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
