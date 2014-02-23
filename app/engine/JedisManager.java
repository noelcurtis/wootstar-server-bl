package engine;

import play.Logger;
import play.Play;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

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
        Logger.info("Initializing Jedis");
        String host = Play.application().configuration().getString("redis.host");
        String port = Play.application().configuration().getString("redis.port");
        JedisPoolConfig pc = new JedisPoolConfig();
        pc.setMinIdle(2);
        pool = new JedisPool(pc, host, Utils.toInt(port, "0"), 20000);
    }

    public JedisPool getPool()
    {
        return pool;
    }

    public void hset(String hashKey, String key, String value)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            jedis.hset(hashKey, key, value);
        }
        catch (Exception ex)
        {
            Logger.error("Error saving to Redis for key:" + key + " value:" + value);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }


    public void set(String key, String value)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            jedis.set(key, value);
        }
        catch (Exception ex)
        {
            Logger.error("Error saving to Redis for key:" + key + " value:" + value);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }

    public String get(String key)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            return jedis.get(key);
        }
        catch (Exception ex)
        {
            Logger.error("Error getting from Redis for key:" + key);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
        return null;
    }

    public String hget(String hashKey, String key)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            return jedis.hget(hashKey, key);
        }
        catch (Exception ex)
        {
            Logger.error("Error getting from Redis for key: " + key + " hash: " + hashKey);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
        return null;
    }

    public Map<String, String> hgetall(String hashKey)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            return jedis.hgetAll(hashKey);
        }
        catch (Exception ex)
        {
            Logger.error("Error getting from Redis for key: " + hashKey);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
        return null;
    }


    public void hdel(String hashKey, String key)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            jedis.hdel(hashKey, key);
        }
        catch (Exception ex)
        {
            Logger.error("Error deleting from Redis for hash:" + hashKey  + " key: " + key);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }

    public void del(String key)
    {
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            jedis.del(key);
        }
        catch (Exception ex)
        {
            Logger.error("Error deleting from Redis for key:" + key);
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }

    public void flush()
    {
        Logger.info("Flushig Redis cache!");
        Jedis jedis = SharedJedisManager().getPool().getResource();
        try
        {
            jedis.flushAll();
            jedis.flushDB();
        }
        catch (Exception ex)
        {
            Logger.error("Error flushing DB");
        }
        finally
        {
            SharedJedisManager().getPool().returnResource(jedis);
        }
    }
}
