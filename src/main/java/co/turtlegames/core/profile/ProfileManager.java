package co.turtlegames.core.profile;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.profile.command.SetRankCommand;
import co.turtlegames.core.profile.listeners.ProfilePreCacheListener;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProfileManager extends TurtleModule {

    public static final PlayerProfile LOADING = new PlayerProfile(null, (UUID) null);

    private DatabaseConnector _dbConnector;

    private AsyncLoadingCache<UUID, PlayerProfile> _profileCache;

    public ProfileManager(TurtleCore pluginInstance) {

        super(pluginInstance, "Profile Manager");

        _profileCache = Caffeine.newBuilder()
                            .expireAfterAccess(15, TimeUnit.MINUTES)
                                .buildAsync(new PlayerProfileLoader(this));

        _dbConnector = this.getDatabaseConnector();

    }

    @Override
    public void initializeModule() {

        this.registerListener(new ProfilePreCacheListener(this));

        this.registerCommand(new SetRankCommand(this));

    }

    public CompletableFuture<PlayerProfile> fetchProfile(UUID uuid) {
        return _profileCache.get(uuid);
    }


}
