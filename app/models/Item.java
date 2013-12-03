package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
//import play.db.ebean.Model;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//@Entity
public class Item implements Serializable
{
    //@javax.persistence.Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    private String Id;

    //@OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> Attributes;

    private Double ListPrice;
    private Integer PurchaseLimit;
    private Double SalePrice;

    @JsonProperty("Attributes")
    public List<Attribute> getAttributes()
    {
        return Attributes;
    }

    @JsonProperty("Attributes")
    public void setAttributes(List<Attribute> attributes)
    {
        Attributes = attributes;
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

    @JsonProperty("ListPrice")
    public Double getListPrice()
    {
        return ListPrice;
    }

    @JsonProperty("ListPrice")
    public void setListPrice(Double listPrice)
    {
        ListPrice = listPrice;
    }

    @JsonProperty("PurchaseLimit")
    public Integer getPurchaseLimit()
    {
        return PurchaseLimit;
    }

    @JsonProperty("PurchaseLimit")
    public void setPurchaseLimit(Integer purchaseLimit)
    {
        PurchaseLimit = purchaseLimit;
    }

    @JsonProperty("SalePrice")
    public Double getSalePrice()
    {
        return SalePrice;
    }

    @JsonProperty("SalePrice")
    public void setSalePrice(Double salePrice)
    {
        SalePrice = salePrice;
    }

    @JsonIgnore
    public String getCondition()
    {
        for (Attribute a : Attributes)
        {
            if (a.getKey().toLowerCase().equals("condition"))
            {
                return a.getValue();
            }
        }
        return null;
    }
}
