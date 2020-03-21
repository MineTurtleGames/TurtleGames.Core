package co.turtlegames.core.stats;

import co.turtlegames.core.stats.action.IncrementStatAction;
import co.turtlegames.core.util.Call;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerStatData {

    private PlayerStatManager _statManager;

    private UUID _owner;
    private Map<String, PlayerStat> _statData;

    public PlayerStatData(PlayerStatManager statManager, UUID owner, Collection<PlayerStat> stats) {

        _statManager = statManager;

        _owner = owner;
        _statData = new HashMap<>();

        for(PlayerStat stat : stats) {

            stat.annex(this);
            _statData.put(stat.getCombinedId(), stat);

        }

    }

    public PlayerStat getStat(String game, String stat) {
        return _statData.getOrDefault(game + "." + stat, this.generateStat(game, stat));
    }

    private PlayerStat generateStat(String game, String stat) {

        PlayerStat statInstance = new PlayerStat(game, stat, 0);
        statInstance.annex(this);

        _statData.put(game + "." + stat, statInstance);
        return statInstance;

    }

    public CompletableFuture<Boolean> incrementStat(PlayerStat stat, double amount) {

        CompletableFuture<Boolean> toComplete = new CompletableFuture<>();

        CompletableFuture<Double> dbResponseFuture =
                _statManager.getDatabaseConnector().executeActionAsync(new IncrementStatAction(_owner, stat.getGame(), stat.getName(), amount));

        dbResponseFuture.thenAccept((Double newValue) -> {

            stat.updateValue(newValue);
            toComplete.complete(true);

        });

        dbResponseFuture.exceptionally(Call.exceptionPassthrough(toComplete::completeExceptionally));

        return null;

    }

}
