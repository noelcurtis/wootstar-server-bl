package engine.woot.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootApiHelpers;
import engine.woot.WootReponseBuilder;
import engine.woot.WootRequest;
import models.ApplicationData;
import models.Event;
import models.EventsHelper;
import models.WootOff;
import play.Logger;
import play.cache.Cache;
import play.libs.Json;

import java.util.*;

import static engine.woot.WootRequestQueue.RequestQueue;

public class AllEventsBuilder implements WootReponseBuilder
{
    private final String etag;
    private final JsonNode response;

    public AllEventsBuilder()
    {
        // get all the requests
        List<WootRequest> requests = RequestQueue().getRequests();
        List<engine.data.apiv1.Event> allMappedEvents = new ArrayList<engine.data.apiv1.Event>();

        for (WootRequest request : requests)
        {
            String cacheIdentifier = WootApiHelpers.getCacheIdentifier(request.eventType, request.site);
            // get the checkpoint id
            //CachedObject co = (CachedObject)play.cache.Cache.get(cacheIdentifier);
            CachedObject co = null;
            // check the key to see if fresh
            String checkpointIdentifier = WootApiHelpers.getCheckpointIdentifier(request.eventType, request.site);
            String dt = (String)Cache.get(checkpointIdentifier);
            Date checkpoint = null;
            if (!Strings.isNullOrEmpty(dt))
            {
                checkpoint = new Date(Long.parseLong(dt));
            }

            // see if there is a checkpoint
            if (co != null && co.getTimestamp() != null && checkpoint != null && !checkpoint.after(co.getTimestamp()))
            {
                Logger.debug("Serving cached events for eventType " + request.eventType + " site " + request.site);
                allMappedEvents.addAll(Arrays.asList(co.getEvents()));
            }
            else
            {
                Logger.debug("Creating cached object for eventType " + request.eventType + " site " + request.site);
                List<Event> allEvents =  EventsHelper.getEvents(request.eventType, request.site);
                List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
                // map events so they can be rendered in json
                for (models.Event e : allEvents)
                {
                    engine.data.apiv1.Event mappedEvent = new engine.data.apiv1.Event(e);
                    if (!mappedEvents.contains(mappedEvent))
                    {
                        mappedEvents.add(mappedEvent);
                    }
                    else
                    {
                        Logger.debug("Duplicate event id " + mappedEvent.Id);
                        mappedEvents.add(mappedEvent);
                    }
                }
                // create a new cached object
                play.cache.Cache.set(cacheIdentifier, new CachedObject(mappedEvents, null, checkpoint));
                allMappedEvents.addAll(mappedEvents);
            }
        }

        if (allMappedEvents.isEmpty())
        {
            Logger.info("No events for request {all events}");
        }

        Logger.info("Responding with events count: " + allMappedEvents.size());
        JsonNode eventsAsJson = WootObjectMapper.WootMapper().valueToTree(allMappedEvents);
        this.etag = Hashing.sha256().hashString(eventsAsJson.toString()).toString();
        this.response = eventsAsJson;
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
