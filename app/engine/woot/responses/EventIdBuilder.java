package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.hash.Hashing;
import engine.Utils;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import models.Event;
import play.libs.Json;

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
        if (mappedEvent != null)
        {
            JsonNode eventAsJson = WootObjectMapper.WootMapper().valueToTree(mappedEvent);
            this.etag = Hashing.sha256().hashString(eventAsJson.toString()).toString();
            this.response = eventAsJson;
        }
        else
        {
            ObjectNode result = Json.newObject();
            result.put("status", "ok");
            result.put("message", "event with " + eventId + " not found");
            this.response = result;
            this.etag = null;
        }
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
