package engine.data.apiv1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import engine.Utils;
import models.*;

import java.util.*;

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
    //public String WriteUp;
    //public String Subtitle;
    // WootPlus
    //public String ManufacturerText;
    public String Url;
    public String Text;

    public String MainPhoto;
    public int SortOrder = Utils.defaultSortOrder;

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
        this.SortOrder = Utils.getEventSortOrder(this.Site);

        if ((!(dataEvent instanceof WootPlus) && ignoreOffers) || !ignoreOffers) // offers ignored for WootPlus events.
        {
            List<models.Offer> dataOffers = dataEvent.getOffers();
            if (dataOffers != null)
            {
                Offers = new ArrayList<Offer>();
                for (models.Offer o : dataOffers)
                {
                    Offer offer = new Offer(o);
                    Offers.add(offer);
//                    if (o.getPhotos() != null && !(o.getPhotos().isEmpty()))
//                    {
//                        List<Photo> photos = o.getPhotos();
//                        Collections.sort(photos, new PhotoComparitor());
//                        this.MainPhoto = photos.get(photos.size() - 1).getUrl();
//                    }
                    this.MainPhoto = offer.MainPhoto;
                }
            }
        }

        if (dataEvent instanceof WootOff)
        {
            //this.WriteUp = ((WootOff) dataEvent).getWriteUp();
            //this.Subtitle = ((WootOff) dataEvent).getSubtitle();
            if (!this.Offers.isEmpty())
            {
                this.Title = this.Offers.get(0).Title; // set event title to offer title
            }
        }

        if (dataEvent instanceof WootPlus)
        {
            //this.ManufacturerText = ((WootPlus) dataEvent).getManufacturerText();
            this.Url = ((WootPlus) dataEvent).getUrl();
            this.Text = Utils.cleanStringOfHtmlTags(((WootPlus) dataEvent).getText());
            // if main photo is still not set for some reason
            if (((WootPlus) dataEvent).getPhotos() != null && !((WootPlus) dataEvent).getPhotos().isEmpty())
            {
                List<WpPhoto> photos = ((WootPlus) dataEvent).getPhotos();
                Collections.sort(photos, new PhotoComparitor());
                this.MainPhoto = photos.get(photos.size() - 1).getUrl();
            }
        }
    }

    private class PhotoComparitor implements Comparator<Photograph>
    {
        @Override
        public int compare(Photograph x, Photograph y) {
            final double xArea = x.getArea();
            final double yArea = y.getArea();

            return xArea < yArea ? -1
                    : xArea > yArea ? 1
                    : 0;
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
