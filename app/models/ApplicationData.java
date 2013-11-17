package models;

import com.avaje.ebean.Ebean;
import com.google.common.base.Strings;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ApplicationData extends Model
{
    @Id
    private String datakey;
    private String datavalue;

    private ApplicationData()
    {

    }

    /**
     * Use to add/update a key value pair
     *
     * @param key
     * @param value
     */
    public static void add(String key, String value)
    {
        if (!Strings.isNullOrEmpty(key) && !Strings.isNullOrEmpty(value))
        {
            ApplicationData found = ApplicationData.find.byId(key);
            if (found != null)
            {
                Ebean.delete(found);
            }
            ApplicationData newData = new ApplicationData();
            newData.datakey = key;
            newData.datavalue = value;
            Logger.info("Creating new pair, key " + key);
            newData.save();
        }
    }

    /**
     * Use to find a key in the table
     *
     * @param key
     * @return
     */
    public static String get(String key)
    {
        ApplicationData found = ApplicationData.find.byId(key);
        if (found != null)
        {
            return found.getDatavalue();
        }
        return null;
    }

    public String getDatakey()
    {
        return datakey;
    }

    public String getDatavalue()
    {
        return datavalue;
    }

    // Declare a finder
    public static Finder<String, ApplicationData> find = new Finder<String, ApplicationData>(
            String.class, ApplicationData.class
    );
}
