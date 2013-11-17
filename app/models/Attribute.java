package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Attribute extends Model
{
    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="attribute_seq")
    private Long id; // not from Woot

    private String Key;
    private String Value;

    @JsonIgnore
    public Long getId()
    {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id)
    {
        this.id = id;
    }

    @JsonProperty("Key")
    public String getKey()
    {
        return Key;
    }

    @JsonProperty("Key")
    public void setKey(String key)
    {
        Key = key;
    }

    @JsonProperty("Value")
    public String getValue()
    {
        return Value;
    }

    @JsonProperty("Value")
    public void setValue(String value)
    {
        Value = value;
    }
}
