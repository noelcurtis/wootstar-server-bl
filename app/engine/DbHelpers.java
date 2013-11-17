package engine;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import engine.woot.WootApiHelpers;
import play.Logger;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DbHelpers
{
    /**
     * Use to truncate all Events with cascade
     *
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
        } catch (Exception ex)
        {
            ex.printStackTrace();
            Logger.error("Error Clearing Database " + ex.toString());
            throw new Exception("Error Clearing Database \n" + ex.toString());
        } finally
        {
            connection.close();
        }
    }

    public static void clearWootData(WootApiHelpers.EventType eventType, WootApiHelpers.Site site) throws Exception
    {
        if (eventType == null && site == null)
        {
            clearWootData(); // clear all the data
        } else
        {
            Connection connection = DB.getConnection();
            try
            {
                if (eventType != null && site != null)
                {
                    PreparedStatement ps = connection.prepareStatement("delete from event where dtype = ? and site = ?");
                    ps.setString(1, eventType.getValue());
                    ps.setString(2, site.getValue());
                    Logger.info("Clearing Database for eventType" + eventType.getValue() + " site " + site.getValue());
                    ps.execute();
                } else if (eventType != null && site == null)
                {
                    PreparedStatement ps = connection.prepareStatement("delete from event where dtype = ?");
                    ps.setString(1, eventType.getValue());
                    Logger.info("Clearing Database for eventType" + eventType.getValue());
                    ps.execute();
                } else if (site != null & eventType == null)
                {
                    PreparedStatement ps = connection.prepareStatement("delete from event where site = ?");
                    ps.setString(1, site.getValue());
                    Logger.info("Clearing Database for site" + site.getValue());
                    ps.execute();
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                Logger.error("Error Clearing Database " + ex.toString());
                throw new Exception("Error Clearing Database \n" + ex.toString());
            } finally
            {
                connection.close();
            }
        }
    }

    public static boolean getAdvisoryLock()
    {
        Logger.info("Trying to get Advisory Lock");
        SqlRow lock = Ebean.createSqlQuery("select pg_try_advisory_lock(1)").findUnique();
        boolean result = lock.getBoolean("pg_try_advisory_lock");
        Logger.info("Advisory Lock state: " + result);
        return result;
    }

    private static boolean releaseAdvisoryLock()
    {
        Logger.info("Releasing Advisory Lock");
        SqlRow lock = Ebean.createSqlQuery("select pg_advisory_unlock(1)").findUnique();
        boolean result = lock.getBoolean("pg_advisory_unlock");
        Logger.info("Releasing Lock state: " + result);
        return result;
    }

}
