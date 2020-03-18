package co.turtlegames.core.profile;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.profile.action.CreatePlayerDataAction;
import co.turtlegames.core.profile.action.FetchPlayerDataAction;
import co.turtlegames.core.profile.listeners.ProfilePreCacheListener;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ProfileManager extends TurtleModule {

    public static final PlayerProfile LOADING = new PlayerProfile(null, (UUID) null);

    private DatabaseConnector _dbConnector;

    private Cache<UUID, PlayerProfile> _profileCache;
    private Cache<UUID, CompletableFuture<PlayerProfile>> _futureCache;

    public ProfileManager(TurtleCore pluginInstance) {

        super(pluginInstance, "Profile Manager");

        _profileCache = CacheBuilder.newBuilder()
                            .expireAfterWrite(1, TimeUnit.MINUTES)
                            .expireAfterAccess(15, TimeUnit.MINUTES)
                                .build();

        _futureCache = CacheBuilder.newBuilder()
                .expireAfterWrite(25, TimeUnit.SECONDS)
                .build();

        _dbConnector = this.getDatabaseConnector();

    }

    @Override
    public void initializeModule() {

        this.registerListener(new ProfilePreCacheListener(this));

    }

    public CompletableFuture<PlayerProfile> fetchProfile(UUID uuid) {

        CompletableFuture<PlayerProfile> response = new CompletableFuture<>();

        PlayerProfile cachedProfile = _profileCache.getIfPresent(uuid);

        if(cachedProfile != null) {

            if (cachedProfile == ProfileManager.LOADING)
                return _futureCache.getIfPresent(uuid);

            response.complete(cachedProfile);
            return response;

        }

        _profileCache.put(uuid, ProfileManager.LOADING);
        _futureCache.put(uuid, response);

        FetchPlayerDataAction fetchPlayerData = new FetchPlayerDataAction(uuid);

        CompletableFuture<FetchPlayerDataAction.PlayerData> playerDataFuture =
                _dbConnector.<FetchPlayerDataAction.PlayerData>executeActionAsync(fetchPlayerData);

        playerDataFuture.exceptionally((Throwable ex) -> {

            ex.printStackTrace();

            if (ex instanceof DatabaseException) {

                DatabaseException dbEx = (DatabaseException) ex;

                if (dbEx.getFailureType() == DatabaseException.FailureType.NOT_FOUND) {

                    CreatePlayerDataAction createQuery = new CreatePlayerDataAction(uuid);

                    CompletableFuture<Boolean> createDataFuture =
                            _dbConnector.<Boolean>executeActionAsync(createQuery);

                    createDataFuture.exceptionally((Throwable throwable) -> {

                        response.completeExceptionally(throwable);

                        _profileCache.invalidate(uuid);
                        _futureCache.invalidate(uuid);

                        return null;

                    });

                    createDataFuture.thenAccept((Boolean success) -> {

                        if(!success) {

                            response.completeExceptionally(new DatabaseException(DatabaseException.FailureType.MISC, "I dont even know"));
                            return;

                        }

                        PlayerProfile newProfile = new PlayerProfile(this, uuid);

                        _profileCache.put(uuid, newProfile);
                        _futureCache.invalidate(uuid);

                        response.complete(newProfile);

                    });

                    return null;

                }

            }

            _profileCache.invalidate(uuid);
            _futureCache.invalidate(uuid);

            response.completeExceptionally(ex);

            return null;

        });

        playerDataFuture.thenAccept((FetchPlayerDataAction.PlayerData playerData) -> {

            PlayerProfile newProfile = new PlayerProfile(this, playerData);

            _profileCache.put(uuid, newProfile);
            response.complete(newProfile);

        });

        return response;

    }


}
