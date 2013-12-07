package models;


import com.fasterxml.jackson.annotation.*;
import engine.woot.WootApiHelpers;
//import play.db.ebean.Model;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "Type")
@JsonSubTypes({@JsonSubTypes.Type(value = Daily.class, name = "Daily"),
        @JsonSubTypes.Type(value = Moofi.class, name = "Moofi"),
        @JsonSubTypes.Type(value = Reckoning.class, name = "Reckoning"),
        @JsonSubTypes.Type(value = WootOff.class, name = "WootOff"),
        @JsonSubTypes.Type(value = WootPlus.class, name = "WootPlus")})
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Entity
public class Event implements Serializable
{
    //@javax.persistence.Id
    private String Id;
    private String Site;
    private Date StartDate;
    private Date EndDate;
    private String Title;

    @Override
    @JsonIgnore
    public boolean equals(Object object)
    {
        boolean isEqual = false;

        if (object != null && object instanceof Event)
        {
            isEqual = this.Id == ((Event) object).Id;
        }

        return isEqual;
    }

    //@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Offer> Offers;

    @JsonProperty("Id")
    public String getId()
    {
        return Id;
    }

    @JsonProperty("Id")
    public void setId(String id)
    {
        Id = id;
    }

    @JsonProperty("Site")
    public String getSite()
    {
        return Site;
    }

    @JsonProperty("Site")
    public void setSite(String site)
    {
        Site = site;
    }

    @JsonProperty("StartDate")
    public Date getStartDate()
    {
        return StartDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(Date startDate)
    {
        StartDate = startDate;
    }

    @JsonProperty("EndDate")
    public Date getEndDate()
    {
        return EndDate;
    }

    @JsonProperty("EndDate")
    public void setEndDate(Date endDate)
    {
        EndDate = endDate;
    }

    @JsonProperty("Title")
    public String getTitle()
    {
        return Title;
    }

    @JsonProperty("Title")
    public void setTitle(String title)
    {
        Title = title;
    }

    @JsonProperty("Offers")
    public List<Offer> getOffers()
    {
        return Offers;
    }

    @JsonProperty("Offers")
    public void setOffers(List<Offer> offers)
    {
        Offers = offers;
    }

//    // Declare a finder
//    public static Finder<String, Event> find = new Finder<String, Event>(
//            String.class, Event.class
//    );
//
//    @JsonIgnore
//    public static boolean wootOffEventsExist()
//    {
//        List<models.Event> foundEvents = models.Event.find.where().eq("dtype", "WootOff").findList();
//        return !foundEvents.isEmpty();
//    }
//
//    // helper to get all events
//    @JsonIgnore
//    public static List<Event> getAllEvents()
//    {
//        // Get a list of the events
//        List<models.Event> events = models.Event.find.all();
//        return events;
//    }
//
//    // helper to get an event by id
//    @JsonIgnore
//    public static Event getEvent(String id)
//    {
//        models.Event foundEvent = models.Event.find.byId(id);
//        return foundEvent;
//    }
//
//    // helper to get events to type
//    @JsonIgnore
//    public static List<Event> getEvents(String type)
//    {
//        List<models.Event> foundEvents = models.Event.find.where().eq("dtype", type).findList();
//        return foundEvents;
//    }
//
//    @JsonIgnore
//    public static List<Event> getEventsBySite(WootApiHelpers.Site site)
//    {
//        return getEvents(null, site);
//    }
//
//    @JsonIgnore
//    public static List<Event> getEventsByType(WootApiHelpers.EventType eventType)
//    {
//        return getEvents(eventType, null);
//    }
//
//    @JsonIgnore
//    public static List<Event> getEvents(WootApiHelpers.EventType eventType, WootApiHelpers.Site site)
//    {
//        List<models.Event> foundEvents = new ArrayList<Event>();
//        if (eventType != null && site != null)
//        {
//            foundEvents = models.Event.find.where().eq("dtype", eventType.getValue()).eq("site", site.getValue()).findList();
//        } else if (eventType != null && site == null)
//        {
//            foundEvents = models.Event.find.where().eq("dtype", eventType.getValue()).findList();
//        } else if (eventType == null && site != null)
//        {
//            foundEvents = models.Event.find.where().eq("site", site.getValue()).findList();
//        }
//        return foundEvents;
//    }
}
