package co.turtlegames.core.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public interface IRedisAction {

    CompletableFuture<Boolean> run(Jedis jedis);

}
