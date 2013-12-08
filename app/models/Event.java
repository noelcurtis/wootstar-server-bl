package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "Type")
@JsonSubTypes({@JsonSubTypes.Type(value = Daily.class, name = "Daily"),
        @JsonSubTypes.Type(value = Moofi.class, name = "Moofi"),
        @JsonSubTypes.Type(value = Reckoning.class, name = "Reckoning"),
        @JsonSubTypes.Type(value = WootOff.class, name = "WootOff"),
        @JsonSubTypes.Type(value = WootPlus.class, name = "WootPlus")})
public class Event implements Serializable
{
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
}
