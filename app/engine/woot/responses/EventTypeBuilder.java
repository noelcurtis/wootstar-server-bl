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
import models.WootOff;
import play.Logger;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static engine.woot.WootRequestQueue.RequestQueue;

public class EventTypeBuilder implements WootReponseBuilder
{
    private final String eventType;
    private final String etag;
    private final JsonNode response;

    public EventTypeBuilder(String eventType)
    {
        this.eventType = eventType;

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
                    List<Event> events;
                    if (request.eventType == WootApiHelpers.EventType.WootOff)
                    {
                        events = WootOff.getAllEvents(); // WootOff events are not stored in the DB
                    }
                    else
                    {
                        events = Event.getEvents(request.eventType, request.site);
                    }
                    List<engine.data.apiv1.Event> mappedEvents = new ArrayList<engine.data.apiv1.Event>();
                    // map events so they can be rendered in json
                    for (models.Event e : events)
                    {
                        engine.data.apiv1.Event mappedEvent = new engine.data.apiv1.Event(e);
                        if (!mappedEvents.contains(mappedEvent))
                        {
                            mappedEvents.add(mappedEvent);
                        }
                        else
                        {
                            Logger.debug("Duplicate event id " + mappedEvent.Id);
                        }
                    }
                    // create a new cached object
                    play.cache.Cache.set(cacheIdentifier, new CachedObject(mappedEvents, null, checkpoint));
                    allMappedEvents.addAll(mappedEvents);
                }
            }

        }

        if (allMappedEvents.isEmpty())
        {
            ObjectNode result = Json.newObject();
            result.put("status", "ok");
            result.put("message", "events for " + eventType + " not found");
            this.response = result;
            this.etag = null;
        }
        else
        {
            Logger.info("Responding with events count: " + allMappedEvents.size());
            JsonNode eventsAsJson = WootObjectMapper.WootMapper().valueToTree(allMappedEvents);
            this.etag = Hashing.sha256().hashString(eventsAsJson.toString()).toString();
            this.response = eventsAsJson;
        }
    }

    @Override
    public JsonNode getResponse()
    {
        return response;
    }


    public String getEtag()
    {
        return this.etag;
    }
}
