package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@JsonIgnoreProperties({"Tags"})
public class WpPhoto extends Model
{
    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="wp_photo_seq")
    public Long id; // not from Woot

    private String Height;
    private String Url;
    private String Width;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

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
}
