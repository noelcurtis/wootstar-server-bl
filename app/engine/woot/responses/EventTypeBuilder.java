package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootApiHelpers;
import engine.woot.WootReponseBuilder;
import models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventTypeBuilder implements WootReponseBuilder
{
    private final String eventType;

    public EventTypeBuilder(String eventType)
    {
        this.eventType = eventType;
    }

    @Override
    public JsonNode getResponse()
    {
        List<Event> foundEvents = Event.getEventsByType(WootApiHelpers.EventType.fromString(this.eventType));
        List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
        // map events so they can be rendered in json
        for (models.Event e : foundEvents)
        {
            mappedEvents.add(new engine.data.apiv1.Event(e));
        }
        JsonNode eventsAsJson = WootObjectMapper.WootMapper().valueToTree(mappedEvents);
        return eventsAsJson;
    }
}
