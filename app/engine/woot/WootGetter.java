package engine.woot;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxType;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.DbHelpers;
import models.ApplicationData;
import models.Event;
import play.Logger;
import play.libs.F;
import play.libs.WS;

import java.util.Date;
import java.util.List;

import static engine.WootObjectMapper.WootMapper;

public class WootGetter
{
    private WootApiHelpers.Site site;
    private WootApiHelpers.EventType eventType;

    public WootGetter(WootRequest request)
    {
        this(request.eventType, request.site);
    }

    public WootGetter(WootApiHelpers.EventType eventType, WootApiHelpers.Site site)
    {
        this.site = site;
        this.eventType = eventType;
    }

    private void createUpdateCheckpoint()
    {
        Date dt = new Date();
        final String id = WootApiHelpers.getCheckpointIdentifier(eventType, site);
        Logger.info("Creating update checkpoint for {" + id + "}");
        ApplicationData.add(id , Long.toString(dt.getTime()));
    }

    private WS.WSRequestHolder constructRequest()
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
                    //@Transactional(type = TxType.REQUIRES_NEW)
                    public void invoke(WS.Response response) throws Throwable
                    {
                        try
                        {
                            final ObjectMapper om = new ObjectMapper(); // map response
                            final List<Event> events = om.readValue(response.getBody(), WootMapper().getTypeFactory().constructCollectionType(List.class, Event.class));
                            if (!events.isEmpty())
                            {
                                DbHelpers.clearWootData(eventType, site);
                                Logger.info("Saving new events start");
                                Ebean.save(events); //save all the events
                                Logger.info("Saving new events end");
                                createUpdateCheckpoint(); // create/update the checkpoint
                                Logger.info("Found " + events.size() + " events for eventType " + eventType + " site " + site);
                            }
                            else
                            {
                                Logger.info("No events for eventType " + eventType + " site " + site);
                            }
                        } catch (Exception ex)
                        {
                            Logger.error("Error Refreshing Database: " + ex.toString());
                            Logger.error("Woot Response status " + response.getStatusText());
                            //Logger.error("Woot Response " + response.getBody());
                            Logger.info("WebServices Async Error: " + "eventType: " + eventType + " site: " + site);
                            ex.printStackTrace();
                        }
                        final long timeTaken = System.currentTimeMillis() - startTime;
                        Logger.info("Getting and saving events took: {" + timeTaken + "}ms");
                    }
                });
    }
}
