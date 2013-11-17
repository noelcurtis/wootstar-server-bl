package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import engine.woot.WootRequest;
import models.Event;

import java.util.List;

import static engine.woot.WootRequestQueue.RequestQueue;

public class AllEventsBuilder implements WootReponseBuilder
{

    @Override
    public JsonNode getResponse()
    {
        // get all the requests
        List<WootRequest> requests = RequestQueue().getRequests();

        ObjectMapper mapper = WootObjectMapper.WootMapper();
        ArrayNode eventsJson = mapper.createArrayNode();

        for (WootRequest request : requests)
        {
            // create a new cached object
            List<Event> events = Event.getEvents(request.eventType, request.site);
            CachedObject ob = new CachedObject(events);
            eventsJson.add(ob.getJson());

            // get CachedObject from cache
        }
        return eventsJson;
    }
}
