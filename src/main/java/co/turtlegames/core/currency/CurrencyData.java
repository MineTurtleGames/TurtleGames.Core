package co.turtlegames.core.currency;

import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CurrencyData {

    private CurrencyManager _manager;
    private UUID _ownerUuid;

    private EnumMap<CurrencyType, Integer> _balances;
    public CurrencyData(CurrencyManager manager, UUID ownerUuid) {

        _manager = manager;
        _ownerUuid = ownerUuid;

        _balances = new EnumMap<>(CurrencyType.class);

    }

    public CompletableFuture<Boolean> setBalance(CurrencyType type, int newBalance) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        CompletableFuture<Boolean> setFuture = _manager.setPlayerCurrency(_ownerUuid, type, newBalance);

        setFuture.exceptionally(ex -> {

            future.completeExceptionally(ex);
            return false;

        });

        setFuture.thenAccept(success -> {

            _balances.put(type, newBalance);
            future.complete(success);

        });

        return future;

    }

    public CompletableFuture<Boolean> updateBalance(CurrencyType type, int deltaBalance) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        CompletableFuture<Boolean> updateFuture = _manager.updatePlayerCurrency(_ownerUuid, type, deltaBalance);

        updateFuture.exceptionally(ex -> {

            future.completeExceptionally(ex);
            return false;

        });

        updateFuture.thenAccept(success -> {

            _balances.put(type, _balances.get(type) + deltaBalance);
            future.complete(success);

        });

        return future;

    }

    public int getBalance(CurrencyType type) {
        return _balances.get(type);
    }

    protected void putBalance(CurrencyType type, int balance) {
        _balances.put(type, balance);
    }

    protected boolean hasBalance(CurrencyType type) {
        return _balances.containsKey(type);
    }

}
