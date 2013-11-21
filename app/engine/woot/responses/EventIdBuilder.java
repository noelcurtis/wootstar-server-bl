package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.hash.Hashing;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import models.Event;

import java.util.List;

public class EventIdBuilder implements WootReponseBuilder
{
    private final String eventId;
    private final String etag;
    private final JsonNode response;

    public EventIdBuilder(String eventId)
    {
        this.eventId = eventId;
        Event foundEvent = Event.getEvent(eventId);
        engine.data.apiv1.Event mappedEvent = new engine.data.apiv1.Event(foundEvent, false);
        JsonNode eventAsJson = WootObjectMapper.WootMapper().valueToTree(mappedEvent);
        this.etag = Hashing.sha256().hashString(eventAsJson.toString()).toString();
        this.response = eventAsJson;
    }

    @Override
    public JsonNode getResponse()
    {
        return this.response;
    }

    public String getEtag()
    {
        return this.etag;
    }
}
