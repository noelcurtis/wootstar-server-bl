package engine.data.apiv1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import engine.Utils;
import models.WootOff;
import models.WootPlus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event
{
    public String Id;
    public String Site;
    public Date StartDate;
    public Date EndDate;
    public String Title;
    public String Type;
    public List<Offer> Offers;
    // WootOff
    public String WriteUp;
    public String Subtitle;
    // WootPlus
    public String ManufacturerText;
    public String Url;
    public String Text;

    public String MainPhoto;

    public Event(models.Event dataEvent)
    {
        this(dataEvent, true);
    }

    public Event(models.Event dataEvent, boolean ignoreOffers)
    {
        this.Id = dataEvent.getId();
        this.Site = dataEvent.getSite();
        this.StartDate = dataEvent.getStartDate();
        this.EndDate = dataEvent.getEndDate();
        this.Title = dataEvent.getTitle();
        this.Type = dataEvent.getClass().getSimpleName().toString();

        if ((!(dataEvent instanceof WootPlus) && ignoreOffers) || !ignoreOffers) // offers ignored for WootPlus events.
        {
            List<models.Offer> dataOffers = dataEvent.getOffers();
            if (dataOffers != null)
            {
                Offers = new ArrayList<Offer>();
                for (models.Offer o : dataOffers)
                {
                    Offers.add(new Offer(o));
                    if (o.getPhotos() != null && !(o.getPhotos().isEmpty()))
                    {
                        this.MainPhoto = o.getPhotos().get(0).getUrl();
                    }
                }
            }
        }

        if (dataEvent instanceof WootOff)
        {
            this.WriteUp = ((WootOff) dataEvent).getWriteUp();
            this.Subtitle = ((WootOff) dataEvent).getSubtitle();
            if (!this.Offers.isEmpty())
            {
                this.Title = this.Offers.get(0).Title; // set event title to offer title
            }
        }

        if (dataEvent instanceof WootPlus)
        {
            this.ManufacturerText = ((WootPlus) dataEvent).getManufacturerText();
            this.Url = ((WootPlus) dataEvent).getUrl();
            this.Text = Utils.cleanStringOfHtmlTags(((WootPlus) dataEvent).getText());
            // if main photo is still not set for some reason
            if (((WootPlus) dataEvent).getPhotos() != null && !((WootPlus) dataEvent).getPhotos().isEmpty())
            {
                MainPhoto = ((WootPlus) dataEvent).getPhotos().get(0).getUrl();
            }
        }
    }

    @Override
    @JsonIgnore
    public boolean equals(Object object)
    {
        boolean isEqual = false;

        if (object != null && object instanceof Event)
        {
            isEqual = this.Id == ((Event) object).Id;
        }

        return isEqual;
    }
}
