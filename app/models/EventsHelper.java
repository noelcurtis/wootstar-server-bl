package models;

import engine.woot.WootApiHelpers;
import engine.woot.WootRequest;
import engine.woot.WootRequestQueue;
import play.Logger;
import play.cache.Cache;

import java.util.ArrayList;
import java.util.List;

public class EventsHelper
{
    public static void saveEvents(List<Event> events, WootRequest request)
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
        List<Event> foundEvents = (List<Event>)Cache.get(WootApiHelpers.getDbIdentifier(request.eventType, request.site));
        if (foundEvents != null)
        {
            return foundEvents;
        }
        return new ArrayList<Event>();
    }

    public static List<Event> getAllEvents()
    {
        List<Event> results = new ArrayList<Event>();
        for (WootRequest request : WootRequestQueue.RequestQueue().getRequests())
        {
            results.addAll(getEvents(request));
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
