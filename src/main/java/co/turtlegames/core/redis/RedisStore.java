package co.turtlegames.core.redis;

import com.sun.org.apache.xpath.internal.operations.Bool;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public abstract class RedisStore {

    private RedisManager _redisManager;

    private String _identifier;

    public RedisStore(RedisManager redisManager, String identifier) {

        _redisManager = redisManager;
        _identifier = identifier;

    }

    protected void runAction(IRedisAction action) {

        Jedis resource = _redisManager.getJedisPool().getResource();
        CompletableFuture<Boolean> future = action.run(resource);

        future.thenAccept((success) -> {
            resource.close();
        });

    }

}
