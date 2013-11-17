package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "Type")
@JsonSubTypes({@JsonSubTypes.Type(value = Daily.class, name = "Daily"),
        @JsonSubTypes.Type(value = Moofi.class, name = "Moofi"),
        @JsonSubTypes.Type(value = Reckoning.class, name = "Reckoning"),
        @JsonSubTypes.Type(value = WootOff.class, name = "WootOff"),
        @JsonSubTypes.Type(value = WootPlus.class, name = "WootPlus")})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class Event extends Model
{
    @javax.persistence.Id
    private String Id;
    private String Site;
    private Date StartDate;
    private Date EndDate;
    private String Title;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
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

    // Declare a finder
    public static Finder<String, Event> find = new Finder<String, Event>(
            String.class, Event.class
    );

    @JsonIgnore
    public static boolean wootOffEventsExist()
    {
        List<models.Event> foundEvents = models.Event.find.where().eq("dtype", "WootOff").findList();
        return !foundEvents.isEmpty();
    }

//    @JsonIgnore
//    public static List<models.Event> getFallbackEvents()
//    {
//        List<models.Event> result = new ArrayList<models.Event>();
//        String eventsAsString = ApplicationData.get(Utils.FallbackEventsDbKey);
//        if (eventsAsString != null)
//        {
//            ObjectMapper om = new ObjectMapper(); // map response
//            try
//            {
//                JsonNode node = om.readTree(eventsAsString);
//                final List<models.Event> fallbackEvents = om.readValue(node, WootMapper().getTypeFactory().constructCollectionType(List.class, models.Event.class));
//                if (fallbackEvents != null && !fallbackEvents.isEmpty())
//                {
//                    Logger.info("Found fallback events");
//                    result = fallbackEvents;
//                }
//                else
//                {
//                    Logger.info("No fallback events");
//                }
//            }
//            catch(Exception ex)
//            {
//                Logger.error(ex.toString());
//            }
//        }
//        return result;
//    }

//    @JsonIgnore
//    public static models.Event getFallbackEvent(String id)
//    {
//        List<models.Event> events = getFallbackEvents();
//        for (models.Event e : events)
//        {
//            if (e.getId().equals(id))
//            {
//                Logger.info("Found fallback event with id " + id);
//                return e;
//            }
//        }
//        Logger.info("No fallback event with id " + id);
//        return null;
//    }
}
