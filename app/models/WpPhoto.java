package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import engine.Utils;

import java.io.Serializable;

@JsonIgnoreProperties({"Tags"})
public class WpPhoto implements Serializable, Photograph
{
    public Long id; // not from Woot

    private String Height;
    private String Url;
    private String Width;

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

    @JsonIgnore
    public double getArea()
    {
        return Utils.toDouble(getHeight(), "0") * Utils.toDouble(getWidth(), "0");
    }
}
