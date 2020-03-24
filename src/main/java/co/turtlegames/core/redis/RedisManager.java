package co.turtlegames.core.redis;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.util.AuthInfo;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

public class RedisManager extends TurtleModule {

    private Jedis _jedis;

    public RedisManager(JavaPlugin plugin) {
        super(plugin, "Redis Manager");
    }

    @Override
    public void initializeModule() {
        _jedis = new Jedis(AuthInfo.MASTER_SERVER_HOST);
    }



}
