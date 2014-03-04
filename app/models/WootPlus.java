package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties({"Subtitle"})
public class WootPlus extends Event
{
    private String ManufacturerText;

    private List<WpPhoto> Photos;

    private String Url;
    private String Text;
    private String DiscussionUrl;

    @JsonProperty("ManufacturerText")
    public String getManufacturerText()
    {
        return ManufacturerText;
    }

    @JsonProperty("ManufacturerText")
    public void setManufacturerText(String manufacturerText)
    {
        ManufacturerText = manufacturerText;
    }

    @JsonIgnore
    public List<WpPhoto> getPhotos()
    {
        return Photos;
    }

    @JsonProperty("Photos")
    public List<String> getPhotoUrls() // Photos being returned as a list of Strings
    {
        List<String> photos = new ArrayList<String>();
        for (WpPhoto p : Photos)
        {
            photos.add(p.getUrl());
        }
        return photos;
    }

    @JsonProperty("Photos")
    public void setPhotos(List<WpPhoto> photos)
    {

        Photos = photos;
    }

    @JsonProperty("Url")
    public String getUrl()
    {
        return Url;
    }

    @JsonProperty("Url")
    public void setUrl(String url)
    {
        this.Url = url;
    }

    @JsonProperty("Text")
    public String getText()
    {
        return Text;
    }

    @JsonProperty("Text")
    public void setText(String text)
    {
        Text = text;
    }

    @JsonProperty("DiscussionUrl")
    public String getDiscussionUrl()
    {
        return DiscussionUrl;
    }

    @JsonProperty("DiscussionUrl")
    public void setDiscussionUrl(String discussionUrl)
    {
        DiscussionUrl = discussionUrl;
    }
}
