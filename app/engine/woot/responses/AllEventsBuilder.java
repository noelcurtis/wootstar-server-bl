package engine.woot.responses;

import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.hash.Hashing;
import engine.WootObjectMapper;
import engine.woot.CachedObject;
import engine.woot.WootApiHelpers;
import engine.woot.WootReponseBuilder;
import engine.woot.WootRequest;
import models.Event;
import models.EventsHelper;
import play.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static engine.metrics.Metrics.WootStarMetrics;
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
            CachedObject co = (CachedObject)play.cache.Cache.get(cacheIdentifier);
            // check the key to see if fresh
            Date checkpoint = EventsHelper.getEventsCheckpointRedis(request);

            // see if there is a checkpoint
            if (co != null && co.getTimestamp() != null && checkpoint != null && !checkpoint.after(co.getTimestamp()))
            {
                Logger.debug("Serving cached events for eventType " + request.eventType + " site " + request.site);
                allMappedEvents.addAll(Arrays.asList(co.getEvents()));
            }
            else
            {
                final long ct = System.currentTimeMillis();
                Logger.debug("Creating cached object for eventType " + request.eventType + " site " + request.site);
                List<Event> allEvents =  EventsHelper.getEventsRedis(request);
                Logger.info("Done getting events: " + (System.currentTimeMillis() - ct) + "ms");
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
                Logger.info("Done creating cached object: " + (System.currentTimeMillis() - ct) + "ms");
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

    public boolean isError()
    {
        return false;
    }
}
