package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import engine.woot.WootApiHelpers;
import play.cache.Cache;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("WootOff")
@Entity
public class WootOff extends Event
{
    private String WriteUp;
    private String Subtitle;

    @JsonProperty("Subtitle")
    public String getSubtitle()
    {
        return Subtitle;
    }

    @JsonProperty("Subtitle")
    public void setSubtitle(String subtitle)
    {
        Subtitle = subtitle;
    }

    @JsonProperty("WriteUp")
    public String getWriteUp()
    {
        return WriteUp;
    }

    @JsonProperty("WriteUp")
    public void setWriteUp(String writeUp)
    {
        WriteUp = writeUp;
    }

    @JsonIgnore
    public static String getWootOffsCacheIdentifier()
    {
        return getWootOffsCacheIdentifier(null);
    }

    @JsonIgnore
    public static String getWootOffsCacheIdentifier(WootApiHelpers.Site site)
    {
        if (site == null)
        {
            return "wootoffs-all-events-identifier";
        }
        return "wootoffs-" + site.toString() + "-events-identifier";

    }

    public static Event getEvent(String id)
    {
        List<Event> wootOffs = getAllEvents();
        for (Event e : wootOffs)
        {
            if(e.getId().equals(id))
            {
                return e;
            }
        }
        return null;
    }

        @JsonIgnore
    public static List<Event> getAllEvents()
    {
        List<Event> wootOffs = (List<Event>)Cache.get(getWootOffsCacheIdentifier());
        if (wootOffs != null)
        {
            return wootOffs;
        }
        return new ArrayList<Event>();
    }

    @JsonIgnore
    public static void saveAll(List<Event> wootOffs)
    {
        Cache.set(getWootOffsCacheIdentifier(), wootOffs);
    }
}
