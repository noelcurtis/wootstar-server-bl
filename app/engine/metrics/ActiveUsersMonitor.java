package engine.metrics;

import com.google.common.base.Strings;
import engine.JedisManager;
import engine.Utils;
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
     * Use to process a web request
     * @param request
     */
    public static void processRequest(Http.Request request)
    {
        //"X-Forwarded-For":["66.108.211.183/cpe-66-108-211-183.nyc.res.rr.com"]
        //"host":["www.wootstar.com"]
        Map<String, String[]> headers = request.headers();
        String hostAddress = null;
        if (headers.containsKey("Host"))
        {
            hostAddress = headers.get("Host")[0];
            if (headers.containsKey("X-Forwarded-For"))
            {
                hostAddress = headers.get("X-Forwarded-For")[0];
            }
        }

        // if no host exists return
        if (Strings.isNullOrEmpty(hostAddress))
        {
            return;
        }

        final Date dt = new Date();
        // Add host to the active users list
        JedisManager.SharedJedisManager().hset(Utils.activeUsersKey, hostAddress, Long.toString(dt.getTime()));
    }

    /**
     * Use to get active users, returns active users over the last minute
     */
    public static int getActiveUsers()
    {
        Map<String, String> users = JedisManager.SharedJedisManager().hgetall(Utils.activeUsersKey);
        Logger.info("Active Users table count: " + users.keySet().size());
        int count = 0;
        for (Map.Entry<String, String> e : users.entrySet())
        {
            DateTime now = new DateTime();
            if (now.getMillis() - Utils.toLong(e.getValue(), "0") <= 60000)
            {
                count++;
            }
            else
            {
                JedisManager.SharedJedisManager().hdel(Utils.activeUsersKey, e.getKey());
            }
        }
        return count;
    }
}
