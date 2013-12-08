package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable
{
    private Long id; // not from Woot
    private String Info;
    private List<Photo> photos;

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
    public String getInfo()
    {
        return Info;
    }

    @JsonIgnore
    public void setInfo(String info)
    {
        this.Info = info;
    }

}
