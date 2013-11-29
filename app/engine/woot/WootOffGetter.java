package engine.woot;

import com.avaje.ebean.TxType;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.DbHelpers;
import models.Event;
import models.WootOff;
import play.Logger;
import play.cache.Cache;
import play.libs.F;
import play.libs.WS;

import java.util.List;

import static engine.WootObjectMapper.WootMapper;

/**
 * The WootOff getter only works to get All WootOff events for now.
 */
public class WootOffGetter extends WootGetter
{
    public WootOffGetter(WootRequest request) throws Exception
    {
        super(request);
        if (request.eventType != WootApiHelpers.EventType.WootOff)
        {
            throw new Exception("WootOffGetter can only be initialized for WootOff Events");
        }

        if (request.site != null)
        {
            throw new Exception("WootOffGetter can only be initialized to get All WootOff events, try setting Site to null in the WootRequest");
        }
    }

    @Override
    public void getEvents()
    {
        final long startTime = System.currentTimeMillis();
        Logger.info("WebServices Async Start: " + "eventType: " + eventType);
        constructRequest().get().onRedeem(
                new F.Callback<WS.Response>()
                {
                    public void invoke(WS.Response response) throws Throwable
                    {
                        try
                        {
                            final ObjectMapper om = new ObjectMapper(); // map response
                            final List<Event> wootOffs = om.readValue(response.getBody(), WootMapper().getTypeFactory().constructCollectionType(List.class, Event.class));
                            WootOff.saveAll(wootOffs);

                            Logger.info("Saving new events end");
                            createUpdateCheckpoint(); // create/update the checkpoint
                            Logger.info("Found " + wootOffs.size() + " events for eventType " + eventType + " site " + site);
                        }
                        catch (Exception ex)
                        {
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
