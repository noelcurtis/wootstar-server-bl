package engine;

import com.google.common.base.Strings;
import play.Play;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager
{
    private final JedisPool pool;

    private static JedisManager _sharedManager;

    public static JedisManager SharedJedisManager()
    {
        if (_sharedManager == null)
        {
            _sharedManager = new JedisManager();
        }
        return _sharedManager;
    }

    private JedisManager()
    {
        String host = Play.application().configuration().getString("redis.host");
        String port = Play.application().configuration().getString("redis.port");
        String password = Play.application().configuration().getString("redis.password");
        if (Strings.isNullOrEmpty(password))
        {
            pool = new JedisPool(new JedisPoolConfig(), host);
        }
        else
        {
            JedisPoolConfig pc = new JedisPoolConfig();
            pc.setMaxActive(3);
            pc.setMaxIdle(2);
            pool = new JedisPool(pc, host, Utils.toInt(port, "0"), 20000, password);
        }
    }

    public JedisPool getPool()
    {
        return pool;
    }
}
