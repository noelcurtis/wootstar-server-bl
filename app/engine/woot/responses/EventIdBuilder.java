package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import models.Event;

import java.util.List;

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
        engine.data.apiv1.Event mappedEvent = new engine.data.apiv1.Event(foundEvent, false);
        JsonNode eventAsJson = WootObjectMapper.WootMapper().valueToTree(mappedEvent);
        return eventAsJson;
    }
}
