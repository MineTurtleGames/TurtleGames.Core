package co.turtlegames.core.profile;

import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.profile.action.CreatePlayerDataAction;
import co.turtlegames.core.profile.action.FetchPlayerDataAction;
import co.turtlegames.core.util.Call;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PlayerProfileLoader implements AsyncCacheLoader<UUID, PlayerProfile> {

    public ProfileManager _profileManager;

    public PlayerProfileLoader(ProfileManager profileManager) {
        _profileManager = profileManager;
    }

    @Override
    public @NonNull CompletableFuture<PlayerProfile> asyncLoad(@NonNull UUID uuid, @NonNull Executor executor) {

        CompletableFuture<PlayerProfile> response = new CompletableFuture<>();
        DatabaseConnector dbConnector = _profileManager.getDatabaseConnector();

        FetchPlayerDataAction fetchPlayerData = new FetchPlayerDataAction(uuid);
        CompletableFuture<FetchPlayerDataAction.PlayerData> playerDataFuture =
                dbConnector.<FetchPlayerDataAction.PlayerData>executeActionAsync(fetchPlayerData);

        playerDataFuture.exceptionally((Throwable ex) -> {

            if(ex instanceof DatabaseException) {

                DatabaseException dbEx = (DatabaseException) ex;

                if (dbEx.getFailureType() == DatabaseException.FailureType.NOT_FOUND) {

                    CreatePlayerDataAction createQuery = new CreatePlayerDataAction(uuid);

                    CompletableFuture<Boolean> createDataFuture =
                            dbConnector.<Boolean>executeActionAsync(createQuery);

                    createDataFuture.exceptionally(Call.exceptionPassthrough(response::completeExceptionally));
                    createDataFuture.thenAccept((Boolean d) -> response.complete(new PlayerProfile(_profileManager, uuid)));

                    return null;

                }

            }

            response.completeExceptionally(ex);
            return null;

        });

        playerDataFuture.thenAccept((FetchPlayerDataAction.PlayerData playerData) -> {
            response.complete(new PlayerProfile(_profileManager, playerData));
        });

        return response;

    }

}
