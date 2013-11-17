package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("WootOff")
@Entity
public class WootOff extends Event
{
    private String WriteUp;
    private String Subtitle;

    @JsonProperty("Subtitle")
    public String getSubtitle()
    {
        return Subtitle;
    }

    @JsonProperty("Subtitle")
    public void setSubtitle(String subtitle)
    {
        Subtitle = subtitle;
    }

    @JsonProperty("WriteUp")
    public String getWriteUp()
    {
        return WriteUp;
    }

    @JsonProperty("WriteUp")
    public void setWriteUp(String writeUp)
    {
        WriteUp = writeUp;
    }
}
