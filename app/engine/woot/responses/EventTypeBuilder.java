package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootApiHelpers;
import engine.woot.WootReponseBuilder;
import engine.woot.WootRequest;
import models.ApplicationData;
import models.Event;
import play.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static engine.woot.WootRequestQueue.RequestQueue;

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
        // get all the requests
        List<WootRequest> requests = RequestQueue().getRequests();
        List<engine.data.apiv1.Event> allMappedEvents = new ArrayList<engine.data.apiv1.Event>();

        for (WootRequest request : requests)
        {
            if (request.eventType == WootApiHelpers.EventType.fromString(this.eventType))
            {
                String cacheIdentifier = WootApiHelpers.getCacheIdentifier(request.eventType, request.site);
                // get the checkpoint id
                CachedObject co = (CachedObject)play.cache.Cache.get(cacheIdentifier);
                // check the key to see if fresh
                String checkpointIdentifier = WootApiHelpers.getCheckpointIdentifier(request.eventType, request.site);
                String dt = ApplicationData.get(checkpointIdentifier);
                Date checkpoint = null;
                if (!Strings.isNullOrEmpty(dt))
                {
                    checkpoint = new Date(Long.parseLong(dt));
                }

                // see if there is a checkpoint
                if (co != null && co.getTimestamp() != null && checkpoint != null && !checkpoint.after(co.getTimestamp()))
                {
                    Logger.debug("Serving cached events for eventType" + request.eventType + " site " + request.site);
                    allMappedEvents.addAll(Arrays.asList(co.getEvents()));
                }
                else
                {
                    Logger.debug("Creating cached object for eventType" + request.eventType + " site " + request.site);
                    // create a new cached object
                    List<Event> events = Event.getEvents(request.eventType, request.site);
                    List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
                    // map events so they can be rendered in json
                    for (models.Event e : events)
                    {
                        mappedEvents.add(new engine.data.apiv1.Event(e));
                    }
                    // create a new cached object
                    play.cache.Cache.set(cacheIdentifier, new CachedObject(mappedEvents, null, checkpoint));
                    allMappedEvents.addAll(mappedEvents);
                }
            }

        }

        if (allMappedEvents.isEmpty())
        {
            Logger.error("No events"); // just log an error in case no events
        }
        JsonNode eventsAsJson = WootObjectMapper.WootMapper().valueToTree(allMappedEvents);
        return eventsAsJson;
    }
}
