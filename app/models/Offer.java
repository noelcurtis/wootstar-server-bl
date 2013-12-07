package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
//import play.db.ebean.Model;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"Stats", "EstimatedShipDate", "Artist", "DiscussionUrl", "WineryDetails"})
//@Entity
public class Offer implements Serializable
{
    //@javax.persistence.Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_seq")
    private String Id;
    private String Features;
    //@OneToMany(cascade = CascadeType.ALL)
    private List<Item> Items;
    private String OriginalStartDate;
    private Integer PercentageRemaining;
    //@ManyToMany(cascade = CascadeType.ALL)
    //@JoinTable(name = "offer_photos")
    private List<Photo> Photos;
    //@OneToMany(cascade = CascadeType.ALL)
    private List<QualityPost> QualityPosts;
    private Integer Rank;
    //@OneToMany(cascade = CascadeType.ALL)
    private List<ShippingMethod> ShippingMethods;
    private String Snippet;
    private Boolean SoldOut;
    private String Specs;
    private String Subtitle;
    private String Teaser;
    private String Title;
    private String Url;
    private String WriteUp;

    // OfferStats is ignored
    //@ManyToOne
    //@JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @JsonIgnore
    public Event getEvent()
    {
        return event;
    }

    @JsonProperty("Features")
    public String getFeatures()
    {
        return Features;
    }

    @JsonProperty("Features")
    public void setFeatures(String features)
    {
        Features = features;
    }

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

    @JsonProperty("Items")
    public List<Item> getItems()
    {
        return Items;
    }

    @JsonProperty("Items")
    public void setItems(List<Item> items)
    {
        Items = items;
    }

    @JsonProperty("OriginalStartDate")
    public String getOriginalStartDate()
    {
        return OriginalStartDate;
    }

    @JsonProperty("OriginalStartDate")
    public void setOriginalStartDate(String originalStartDate)
    {
        OriginalStartDate = originalStartDate;
    }

    @JsonProperty("PercentageRemaining")
    public Integer getPercentageRemaining()
    {
        return PercentageRemaining;
    }

    @JsonProperty("PercentageRemaining")
    public void setPercentageRemaining(Integer percentageRemaining)
    {
        PercentageRemaining = percentageRemaining;
    }

    @JsonIgnore
    public List<Photo> getPhotos()
    {
        return Photos;
    }

    @JsonProperty("Photos")
    public List<String> getPhotoUrls() // Photos being returned as a list of Strings
    {
        List<String> photos = new ArrayList<String>();
        for (Photo p : Photos)
        {
            if (p.getTags().contains("gallery")) // only add photos with gallery tag
            {
                photos.add(p.getUrl());
            }
        }
        return photos;
    }

    @JsonProperty("Photos")
    public void setPhotos(List<Photo> photos)
    {
        Photos = photos;
    }

    @JsonProperty("QualityPosts")
    public List<QualityPost> getQualityPosts()
    {
        return QualityPosts;
    }

    @JsonProperty("QualityPosts")
    public void setQualityPosts(List<QualityPost> qualityPosts)
    {
        QualityPosts = qualityPosts;
    }

    @JsonProperty("Rank")
    public Integer getRank()
    {
        return Rank;
    }

    @JsonProperty("Rank")
    public void setRank(Integer rank)
    {
        Rank = rank;
    }

    @JsonProperty("ShippingMethods")
    public List<ShippingMethod> getShippingMethods()
    {
        return ShippingMethods;
    }

    @JsonProperty("ShippingMethods")
    public void setShippingMethods(List<ShippingMethod> shippingMethods)
    {
        ShippingMethods = shippingMethods;
    }

    @JsonProperty("Snippet")
    public String getSnippet()
    {
        return Snippet;
    }

    @JsonProperty("Snippet")
    public void setSnippet(String snippet)
    {
        Snippet = snippet;
    }

    @JsonProperty("SoldOut")
    public Boolean getSoldOut()
    {
        return SoldOut;
    }

    @JsonProperty("SoldOut")
    public void setSoldOut(Boolean soldOut)
    {
        SoldOut = soldOut;
    }

    @JsonProperty("Specs")
    public String getSpecs()
    {
        return Specs;
    }

    @JsonProperty("Specs")
    public void setSpecs(String specs)
    {
        Specs = specs;
    }

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

    @JsonProperty("Teaser")
    public String getTeaser()
    {
        return Teaser;
    }

    @JsonProperty("Teaser")
    public void setTeaser(String teaser)
    {
        Teaser = teaser;
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

    @JsonProperty("Url")
    public String getUrl()
    {
        return Url;
    }

    @JsonProperty("Url")
    public void setUrl(String url)
    {
        Url = url;
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
}
