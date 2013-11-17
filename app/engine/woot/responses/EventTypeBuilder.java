package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import engine.woot.CachedObject;
import engine.woot.WootApiHelpers;
import engine.woot.WootReponseBuilder;
import models.Event;

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
        CachedObject ob = new CachedObject(foundEvents);
        return ob.getJson();
    }
}
