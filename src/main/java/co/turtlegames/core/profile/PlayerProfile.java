package co.turtlegames.core.profile;

import co.turtlegames.core.achievement.AchievementData;
import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyManager;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.infraction.InfractionData;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.inventory.InventoryData;
import co.turtlegames.core.inventory.InventoryManager;
import co.turtlegames.core.profile.action.AddXpAction;
import co.turtlegames.core.profile.action.FetchPlayerDataAction;
import co.turtlegames.core.profile.action.UpdateProfileRankAction;
import co.turtlegames.core.stats.PlayerStatData;
import co.turtlegames.core.stats.PlayerStatManager;
import co.turtlegames.core.util.Call;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class PlayerProfile {

    private ProfileManager _profileManager;
    private UUID _owner;

    private Rank _rank;
    private long _xp;

    private InfractionData _infractionData = null;
    private AchievementData _achievementData = null;
    private CurrencyData _currencyData = null;
    private PlayerStatData _statData = null;
    private InventoryData _inventoryData = null;

    public PlayerProfile(ProfileManager profileManager, FetchPlayerDataAction.PlayerData data) {

        _profileManager = profileManager;

        Map<String, Object> dataValues = data.getData();

        _owner = UUID.fromString((String) dataValues.get("uuid"));
        _rank = Rank.valueOf((String) dataValues.get("rank"));
        _xp = (long) data.getData().get("xp");

    }

    public PlayerProfile(ProfileManager profileManager, UUID uuid) {

        _profileManager = profileManager;

        _owner = uuid;

        _rank = Rank.PLAYER;
        _xp = 0;

    }

    public UUID getOwnerUuid() {
        return _owner;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(_owner);
    }

    public boolean isOnline() {
        return this.getOwner() != null;
    }

    public Rank getRank() {
        return _rank;
    }

    public long getXp() {
        return _xp;
    }

    public CurrencyData getCurrencyData() {
        return _currencyData;
    }

    public CompletableFuture<CurrencyData> fetchCurrencyData() {

        CompletableFuture<CurrencyData> future = new CompletableFuture<>();

        if (_currencyData != null) {

            future.complete(_currencyData);
            return future;
        }

        CurrencyManager currencyManager = _profileManager.getModule(CurrencyManager.class);

        CompletableFuture<CurrencyData> dataFuture = currencyManager.fetchPlayerCurrencies(_owner);

        dataFuture.exceptionally((ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        dataFuture.thenAccept(currencyData -> {

            _currencyData = currencyData;
            future.complete(currencyData);

        });

        return future;

    }

    public InventoryData getInventoryData() {
        return _inventoryData;
    }

    public CompletableFuture<InventoryData> fetchInventoryData() {

        CompletableFuture<InventoryData> future = new CompletableFuture<>();

        if (_inventoryData != null) {

            future.complete(_inventoryData);
            return future;
        }

        InventoryManager inventoryManager = _profileManager.getModule(InventoryManager.class);

        CompletableFuture<InventoryData> dataFuture = inventoryManager.fetchInventoryData(_owner);

        dataFuture.exceptionally((ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        dataFuture.thenAccept(inventoryData -> {

            _inventoryData = inventoryData;
            future.complete(inventoryData);

        });

        return future;

    }

    public InfractionData getInfractionData() {
        return _infractionData;
    }

    public CompletableFuture<InfractionData> fetchInfractionData() {

        CompletableFuture<InfractionData> future = new CompletableFuture<>();

        if(_infractionData != null) {

            future.complete(_infractionData);
            return future;

        }

        InfractionManager infractionManager = _profileManager.getModule(InfractionManager.class);
        CompletableFuture<InfractionData> dataFuture = infractionManager.fetchInfractionData(_owner);

        dataFuture.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        dataFuture.thenAccept((InfractionData data) -> {

            _infractionData = data;
            future.complete(data);

        });

        return future;

    }

    public CompletableFuture<Boolean> updateRank(Rank rank) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        UpdateProfileRankAction updateRank = new UpdateProfileRankAction(_owner, rank);
        CompletableFuture<Boolean> updateFuture = _profileManager.getDatabaseConnector().executeActionAsync(updateRank);

        updateFuture.thenAccept((Boolean success) -> {

            if(!success) {
                future.completeExceptionally(new Exception("jeff"));
                return;
            }

            _rank = rank;
            future.complete(true);

        });

        updateFuture.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

    public CompletableFuture<Boolean> modifyCurrency(CurrencyType type, int amount) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        CompletableFuture<CurrencyData> dataFuture = fetchCurrencyData();

        dataFuture.thenAccept(data -> {

            data.setBalance(type, data.getBalance(type) + amount);

        });

        return future;

    }

    public CompletableFuture<Boolean> addXp(long amount) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        AddXpAction xpAction = new AddXpAction(_owner, amount);
        CompletableFuture<Long> addFuture = _profileManager.getDatabaseConnector()
                                                    .executeActionAsync(xpAction);

        addFuture.thenAccept((Long newXp) -> {

            _xp = newXp;
            future.complete(true);

        });

        addFuture.exceptionally((Throwable ex) -> {

            ex.printStackTrace();

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

    public boolean canConnect() {
        return true;
    }

    public CompletableFuture<AchievementData> fetchAchievementData() {

        CompletableFuture<AchievementData> future = new CompletableFuture<AchievementData>();
        AchievementManager achievementManager = _profileManager.getModule(AchievementManager.class);

        if(_achievementData != null) {

            future.complete(_achievementData);
            return future;

        }

        CompletableFuture<AchievementData> achievementDataFuture = achievementManager.fetchAchievementData(_owner);

        achievementDataFuture.thenAccept((AchievementData data) -> {

            _achievementData = data;
            future.complete(data);

        });

        achievementDataFuture.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

    public CompletableFuture<PlayerStatData> fetchStatData() {

        CompletableFuture<PlayerStatData> future = new CompletableFuture<PlayerStatData>();
        PlayerStatManager statManager = _profileManager.getModule(PlayerStatManager.class);

        if(_statData != null) {

            future.complete(_statData);
            return future;

        }

        CompletableFuture<PlayerStatData> statDataFuture = statManager.fetchStatData(_owner);

        statDataFuture.thenAccept((PlayerStatData data) -> {

            _statData = data;
            future.complete(data);

        });

        statDataFuture.exceptionally(Call.exceptionPassthrough(future::completeExceptionally));

        return future;

    }

}
