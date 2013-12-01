package engine.woot;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Event;
import models.EventsHelper;
import play.Logger;
import play.cache.Cache;
import play.libs.F;
import play.libs.WS;

import java.util.Date;
import java.util.List;

import static engine.WootObjectMapper.WootMapper;

public class WootGetterZ
{
    protected final WootApiHelpers.Site site;
    protected final WootApiHelpers.EventType eventType;
    protected final WootRequest wootRequest;

    public WootGetterZ(WootRequest request)
    {
        this.wootRequest = request;
        this.site = request.site;
        this.eventType = request.eventType;
    }

    protected void createUpdateCheckpoint()
    {
        Date dt = new Date();
        final String id = WootApiHelpers.getCheckpointIdentifier(eventType, site);
        Logger.info("Creating update checkpoint for {" + id + "}");
        Cache.set(id, Long.toString(dt.getTime()));
    }

    protected WS.WSRequestHolder constructRequest()
    {
        WS.WSRequestHolder requestHolder = WS.url(WootApiHelpers.wootUrl);
        requestHolder.setTimeout(10000); // timeout set to 10 seconds.
        requestHolder.setQueryParameter("key", WootApiHelpers.wootKey); // set the woot key
        if (eventType != null)
        {
            requestHolder.setQueryParameter("eventType", eventType.getValue());
        }
        if (site != null)
        {
            requestHolder.setQueryParameter("site", site.getValue());
        }
        return requestHolder;
    }

    public void getEvents()
    {
        final long startTime = System.currentTimeMillis();
        Logger.info("WebServices Async Start: " + "eventType: " + eventType + " site: " + site);
        constructRequest().get().onRedeem(
                new F.Callback<WS.Response>()
                {
                    @Override
                    public void invoke(WS.Response response) throws Throwable
                    {
                        try
                        {
                            final long responseTimer = System.currentTimeMillis() - startTime;
                            Logger.info("WebServices Async Recieved Response: " + "eventType: " + eventType + " site: " + site + " took: {" + responseTimer + "}ms");
                            final ObjectMapper om = new ObjectMapper(); // map response
                            final List<Event> events = om.readValue(response.getBody(), WootMapper().getTypeFactory().constructCollectionType(List.class, Event.class));

                            EventsHelper.saveEvents(events, wootRequest);
                            createUpdateCheckpoint(); // create/update the checkpoint
                        }
                        catch (Exception ex)
                        {
                            Logger.error("Error Refreshing Events: " + ex.toString());
                            Logger.error("Woot Response status " + response.getStatusText());
                            Logger.debug("Woot Response " + response.getBody());
                            Logger.info("WebServices Async Error: " + "eventType: " + eventType + " site: " + site);
                            ex.printStackTrace();
                        }
                        finally
                        {
                            final long timeTaken = System.currentTimeMillis() - startTime;
                            Logger.info("WebServices Async End: " + "eventType: " + eventType + " site: " + site + " took: {" + timeTaken + "}ms");
                        }
                    }
                });
    }
}
