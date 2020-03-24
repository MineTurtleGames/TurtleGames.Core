package co.turtlegames.core.redis;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.util.AuthInfo;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager extends TurtleModule {

    private JedisPool _jedisPool;

    public RedisManager(JavaPlugin plugin) {
        super(plugin, "Redis Manager");
    }

    @Override
    public void initializeModule() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(5);
        config.setMaxTotal(20);
        _jedisPool = new JedisPool(config, AuthInfo.MASTER_SERVER_HOST);

    }

    @Override
    public void deinitializeModule() {
        _jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return _jedisPool;
    }
}
