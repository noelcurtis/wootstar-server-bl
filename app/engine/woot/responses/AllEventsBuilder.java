package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootReponseBuilder;
import engine.woot.WootRequest;
import models.Event;

import java.util.ArrayList;
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
        List<engine.data.apiv1.Event> allMappedEvents = new ArrayList<engine.data.apiv1.Event>();

        for (WootRequest request : requests)
        {
            // create a new cached object
            List<Event> events = Event.getEvents(request.eventType, request.site);
            List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
            // map events so they can be rendered in json
            for (models.Event e : events)
            {
                mappedEvents.add(new engine.data.apiv1.Event(e));
            }
            allMappedEvents.addAll(mappedEvents);
        }
        JsonNode eventsAsJson = WootObjectMapper.WootMapper().valueToTree(allMappedEvents);
        return eventsAsJson;
    }
}
