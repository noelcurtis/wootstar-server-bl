package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import models.Event;

public class EventIdBuilder implements WootReponseBuilder
{
    private final String eventId;

    public EventIdBuilder(String eventId)
    {
        this.eventId = eventId;
    }

    @Override
    public JsonNode getResponse()
    {
        Event foundEvent = Event.getEvent(eventId);
        CachedObject ob = new CachedObject(foundEvent);
        return ob.getJson();
    }
}
