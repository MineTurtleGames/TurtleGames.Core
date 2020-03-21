package co.turtlegames.core.stats;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.stats.action.FetchStatDataAction;
import co.turtlegames.core.util.Call;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerStatManager extends TurtleModule {

    public PlayerStatManager(JavaPlugin plugin) {
        super(plugin, "Stats Manager");
    }

    @Override
    public void initializeModule() {

    }

    public CompletableFuture<PlayerStatData> fetchStatData(UUID uuid) {

        CompletableFuture<PlayerStatData> toComplete = new CompletableFuture<>();
        FetchStatDataAction statDataAction = new FetchStatDataAction(uuid);

        CompletableFuture<Collection<PlayerStat>> dbResponse =
                this.getDatabaseConnector().executeActionAsync(statDataAction);

        dbResponse.thenAccept((Collection<PlayerStat> stats) -> {

            PlayerStatData data = new PlayerStatData(this, uuid, stats);
            toComplete.complete(data);

        });

        dbResponse.exceptionally(Call.exceptionPassthrough(toComplete::completeExceptionally));

        return toComplete;

    }

}
