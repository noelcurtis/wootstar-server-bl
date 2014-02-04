package engine.metrics;

import com.google.common.base.Strings;
import engine.JedisManager;
import engine.Utils;
import engine.metrics.data.ActiveUsersData;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Minutes;
import play.Logger;
import play.mvc.Http;

import java.util.Date;
import java.util.Map;

public class ActiveUsersMonitor
{
    /**
     * Use to start a session
     * @param request
     */
    public static void startSession(Http.Request request)
    {
        Date date = new Date();
        JedisManager.SharedJedisManager().hset(Utils.activeUsersKey, Utils.getHost(request), Long.toString(date.getTime()));
    }

    /**
     * Use to end a session
     * @param request
     */
    public static void endSession(Http.Request request)
    {
        Date date = new Date();
        JedisManager.SharedJedisManager().hdel(Utils.activeUsersKey, Utils.getHost(request));
    }

    /**
     * Use to get active users, returns active users over the last minute
     */
    public static ActiveUsersData getActiveUsers()
    {
        final Map<String, String> users = JedisManager.SharedJedisManager().hgetall(Utils.activeUsersKey);
        int avg = 0;
        long now = new Date().getTime();
        for(Map.Entry<String, String> user : users.entrySet())
        {
            avg += now - Utils.toLong(user.getValue(), "0");
        }
        if (users.size() > 0)
        {
            return new ActiveUsersData(users.size(), avg/users.size());
        }
        else
        {
            return new ActiveUsersData(0, 0);
        }
    }
}
