package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Tag extends Model
{
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq")
    private Long id; // not from Woot
    private String Info;

    @ManyToMany(mappedBy = "Tags")
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
