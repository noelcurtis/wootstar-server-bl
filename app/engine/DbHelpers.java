package engine;

import play.Logger;
import play.db.DB;

import java.sql.Connection;

public class DbHelpers
{
    /**
     * Use to truncate all Events with cascade
     * @throws Exception
     */
    public static void clearWootData() throws Exception
    {
        Logger.info("Clearing Database..");
        Connection connection = DB.getConnection();
        try
        {
            // use raw JDBC
            connection.prepareStatement("truncate attribute, quality_post, shipping_method, tag, photo_tags, offer_photos, wp_photo, event, offer, item, photo").execute();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Logger.error("Error Clearing Database " + ex.toString());
            throw new Exception("Error Clearing Database \n" + ex.toString());
        }
        finally
        {
            connection.close();
        }
    }

}
