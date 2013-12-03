package models;

import engine.Utils;
import engine.woot.WootApiHelpers;
import engine.woot.WootRequest;
import engine.woot.WootRequestQueue;
import play.Logger;
import play.cache.Cache;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static engine.JedisManager.SharedJedisManager;

public class EventsHelper
{
    public static void saveEventsRedis(ArrayList<Event> events, WootRequest request)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            if(events == null)
            {
                events = new ArrayList<Event>();
            }
            Logger.info("Saving events for request {" + request.toString() + "} count: " + events.size());
            jedis.set(WootApiHelpers.getDbIdentifier(request.eventType, request.site), Utils.serializeToString(events));
        }
        catch (Exception ex)
        {
            Logger.error("Error saving Events to Redis for " + request.toString());
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }

    public static List<Event> getEventsRedis(WootRequest request)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            long ct = System.currentTimeMillis();
            String found =  jedis.get(WootApiHelpers.getDbIdentifier(request.eventType, request.site));
            long diff = System.currentTimeMillis() - ct;
            Logger.info("Getting events from Redis took: {" + diff + "ms}");
            ArrayList<Event> foundEvents = (ArrayList<Event>)Utils.deserializeFromString(found);
            if (foundEvents != null)
            {
                return foundEvents;
            }
        }
        catch (Exception ex)
        {
            Logger.error("Error getting Events from Redis for " + request.toString());
            Logger.info(ex.toString());
            ex.printStackTrace();
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
        return new ArrayList<Event>();
    }

    public static void saveEvents(ArrayList<Event> events, WootRequest request) throws IOException
    {
        if(events == null)
        {
            events = new ArrayList<Event>();
        }
        Logger.info("Saving events for request {" + request.toString() + "} count: " + events.size());
        Cache.set(WootApiHelpers.getDbIdentifier(request.eventType, request.site), events);
    }

    public static List<Event> getEvents(WootRequest request)
    {
        try
        {
            ArrayList<Event> foundEvents = (ArrayList<Event>)Cache.get(WootApiHelpers.getDbIdentifier(request.eventType, request.site));
            if (foundEvents != null)
            {
                return foundEvents;
            }
        }
        catch (Exception ex)
        {
            Logger.info(ex.toString());
            ex.printStackTrace();
        }

        return new ArrayList<Event>();
    }

    public static List<Event> getAllEvents()
    {
        ArrayList<Event> results = new ArrayList<Event>();
        for (WootRequest request : WootRequestQueue.RequestQueue().getRequests())
        {
            results.addAll(getEventsRedis(request));
        }
        return results;
    }

    public static Event getEventById(String id)
    {
        return filterEventsById(id, getAllEvents());
    }

    public static Event filterEventsById(String id, List<Event> allEvents)
    {
        for(Event e : allEvents)
        {
            if (e.getId().equals(id))
            {
                return e;
            }
        }
        return null;
    }

    public static List<Event> getEventsBySite(WootApiHelpers.Site site)
    {
        return filterEventsBySite(site, getAllEvents());
    }

    public static List<Event> filterEventsBySite(WootApiHelpers.Site site, List<Event> allEvents)
    {
        List<Event> results = new ArrayList<Event>();
        for (Event e : allEvents)
        {
            if (e.getSite().equals(site.getValue()))
            {
                results.add(e);
            }
        }
        return results;
    }

    public static List<Event> getEventsByType(WootApiHelpers.EventType eventType)
    {
        return filterEventsByType(eventType, getAllEvents());
    }

    public static List<Event> filterEventsByType(WootApiHelpers.EventType eventType, List<Event> allEvents)
    {
        List<Event> results = new ArrayList<Event>();
        for (Event o : allEvents)
        {
            if (WootApiHelpers.EventType.oIsInstanceOfEventType(o, eventType))
            {
                results.add(o);
            }
        }
        return results;
    }

    public static List<Event> getEvents(WootApiHelpers.EventType eventType, WootApiHelpers.Site site)
    {
        return filterEvents(eventType, site, getAllEvents());
    }

    public static List<Event> filterEvents(WootApiHelpers.EventType eventType, WootApiHelpers.Site site, List<Event> allEvents)
    {
        List<models.Event> foundEvents = new ArrayList<Event>();
        if (eventType != null && site != null)
        {
            foundEvents = filterEventsBySiteAndType(eventType, site, allEvents);
        } else if (eventType != null && site == null)
        {
            foundEvents = filterEventsByType(eventType, allEvents);
        } else if (eventType == null && site != null)
        {
            foundEvents = filterEventsBySite(site, allEvents);
        }
        return foundEvents;
    }

    public static List<Event> filterEventsBySiteAndType(WootApiHelpers.EventType eventType, WootApiHelpers.Site site, List<Event> allEvents)
    {
        List<Event> results = new ArrayList<Event>();
        for (Event e : allEvents)
        {
            if (WootApiHelpers.EventType.oIsInstanceOfEventType(e, eventType) && site.getValue().equals(e.getSite()))
            {
                results.add(e);
            }
        }
        return results;
    }
}
