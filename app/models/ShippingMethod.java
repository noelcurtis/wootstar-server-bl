package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//@Entity
public class ShippingMethod implements Serializable
{

    public Long id; // not from Woot

    private String Name;
    private String PerItemAmount;
    private String PerOrderAmount;
    private String PerEventAmount;


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

    @JsonProperty("Name")
    public String getName()
    {
        return Name;
    }

    @JsonProperty("Name")
    public void setName(String name)
    {
        Name = name;
    }

    @JsonProperty("PerItemAmount")
    public String getPerItemAmount()
    {
        return PerItemAmount;
    }

    @JsonProperty("PerItemAmount")
    public void setPerItemAmount(String perItemAmount)
    {
        PerItemAmount = perItemAmount;
    }

    @JsonProperty("PerOrderAmount")
    public String getPerOrderAmount()
    {
        return PerOrderAmount;
    }

    @JsonProperty("PerOrderAmount")
    public void setPerOrderAmount(String perOrderAmount)
    {
        PerOrderAmount = perOrderAmount;
    }

    @JsonProperty("PerEventAmount")
    public String getPerEventAmount()
    {
        return PerEventAmount;
    }

    @JsonProperty("PerEventAmount")
    public void setPerEventAmount(String perEventAmount)
    {
        PerEventAmount = perEventAmount;
    }
}
