package engine.data.apiv1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Item;
import models.Photo;
import models.QualityPost;
import models.ShippingMethod;

import java.util.List;

public class Offer
{
    public String Condition;
    public String Features;
    public String Id;
    public Double ListPrice;
    public String MainPhoto;
    public String OriginalStartDate;
    public Integer PercentageRemaining;
    public List<String> Photos;
    public List<QualityPost> QualityPosts;
    public Integer Rank;
    public Double SalePrice;
    public List<ShippingMethod> ShippingMethods;
    public String Snippet;
    public Boolean SoldOut;
    public String Specs;
    public String Teaser;
    public String Title;
    public String Url;
    public String WriteUp;

    @JsonIgnore
    public Offer(models.Offer offerModel)
    {
        List<Item> items = offerModel.getItems();
        if (items != null && !items.isEmpty())
        {
            Item firstItem = items.get(0); // get the first item
            Condition = firstItem.getCondition();
            Features = offerModel.getFeatures();
            Id = offerModel.getId();
            ListPrice = firstItem.getListPrice();
            Photos = offerModel.getPhotoUrls();
            List<Photo> photos = offerModel.getPhotos();
            for (Photo photo : photos)
            {
                if (photo.getTags().contains("fullsize-0")) // main photo is selected as on with "fullsize-0" tag
                {
                    MainPhoto = photo.getUrl();
                }
            }
            // if main photo is still not set for some reason
            if (MainPhoto == null && photos != null && !photos.isEmpty())
            {
                MainPhoto = photos.get(0).getUrl();
            }
            OriginalStartDate = offerModel.getOriginalStartDate();
            PercentageRemaining = offerModel.getPercentageRemaining();
            QualityPosts = offerModel.getQualityPosts();
            Rank = offerModel.getRank();
            SalePrice = firstItem.getSalePrice();
            ShippingMethods = offerModel.getShippingMethods();
            Snippet = offerModel.getSnippet();
            SoldOut = offerModel.getSoldOut();
            Specs = offerModel.getSpecs();
            Teaser = offerModel.getTeaser();
            Title = offerModel.getTitle();
            Url = offerModel.getUrl();
            WriteUp = offerModel.getWriteUp();
        }
    }
}
