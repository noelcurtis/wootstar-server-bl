package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Photo extends Model
{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_seq")
    public Long id; // not from Woot

    private String Height;
    private String Url;
    private String Width;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "photo_tags")
    private List<Tag> Tags;

    @ManyToMany(mappedBy = "Photos")
    private List<Offer> offers;

    @JsonIgnore
    public Long getId()
    {
        return id;
    }

    @JsonIgnore
    public void setId(Long id)
    {
        this.id = id;
    }

    //@JsonProperty("Height")
    @JsonIgnore
    public String getHeight()
    {
        return Height;
    }

    @JsonProperty("Height")
    public void setHeight(String height)
    {
        Height = height;
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

    //@JsonProperty("Width")
    @JsonIgnore
    public String getWidth()
    {
        return Width;
    }

    @JsonProperty("Width")
    public void setWidth(String width)
    {
        Width = width;
    }

    @JsonProperty("Tags")
    public List<String> getTags() // Tags being returned as a list of Strings
    {
        List<String> tags = new ArrayList<String>();
        for (Tag t : Tags)
        {
            tags.add(t.getInfo());
        }
        return tags;
    }

    @JsonProperty("Tags")
    public void setTags(List<String> tags)
    {
        this.Tags = new ArrayList<Tag>();
        for (String t : tags) // add all the tags
        {
            Tag tag = new Tag();
            tag.setInfo(t);
            this.Tags.add(tag);
        }
    }
}
