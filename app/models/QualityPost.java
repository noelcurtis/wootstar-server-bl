package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Entity
public class QualityPost extends Model
{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quality_post_seq")
    public Long id; // not from Woot

    private String Avatar;
    private Date PostedOn;
    private String Text;
    private String Username;

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

    @JsonProperty("Avatar")
    public String getAvatar()
    {
        return Avatar;
    }

    @JsonProperty("Avatar")
    public void setAvatar(String avatar)
    {
        Avatar = avatar;
    }

    @JsonProperty("PostedOn")
    public Date getPostedOn()
    {
        return PostedOn;
    }

    @JsonProperty("PostedOn")
    public void setPostedOn(Date postedOn)
    {
        PostedOn = postedOn;
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

    @JsonProperty("Username")
    public String getUsername()
    {
        return Username;
    }

    @JsonProperty("Username")
    public void setUsername(String username)
    {
        Username = username;
    }
}
