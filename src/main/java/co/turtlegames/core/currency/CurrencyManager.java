package co.turtlegames.core.currency;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.currency.action.FetchPlayerCurrenciesAction;
import co.turtlegames.core.currency.action.InsertDefaultBalanceAction;
import co.turtlegames.core.currency.action.SetPlayerCurrencyAction;
import co.turtlegames.core.currency.command.BalanceCommand;
import co.turtlegames.core.currency.command.CurrencyCommand;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.db.DatabaseException;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CurrencyManager extends TurtleModule {

    private DatabaseConnector _dbConnector;

    public CurrencyManager(JavaPlugin plugin) {
        super(plugin, "Currency");
    }

    @Override
    public void initializeModule() {

        _dbConnector = this.getDatabaseConnector();

        registerCommand(new BalanceCommand(this));
        registerCommand(new CurrencyCommand(this));

    }

    public CompletableFuture<Boolean> setPlayerCurrency(UUID uuid, CurrencyType type, int newBalance) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        SetPlayerCurrencyAction action = new SetPlayerCurrencyAction(uuid, type, newBalance);

        CompletableFuture<Boolean> dataFuture = _dbConnector.executeActionAsync(action);

        dataFuture.exceptionally(future::completeExceptionally);
        dataFuture.thenAccept(future::complete);

        return future;

    }

    public CompletableFuture<CurrencyData> fetchPlayerCurrencies(UUID uuid) {

        CompletableFuture<CurrencyData> future = new CompletableFuture<>();

        CurrencyData data = new CurrencyData(this, uuid);
        FetchPlayerCurrenciesAction action = new FetchPlayerCurrenciesAction(uuid);

        CompletableFuture<EnumMap<CurrencyType, Integer>> dataFuture = _dbConnector.executeActionAsync(action, true);

        dataFuture.thenAccept(balances -> {

            for (Map.Entry<CurrencyType, Integer> balance : balances.entrySet()) {
                data.putBalance(balance.getKey(), balance.getValue());
            }

            for (CurrencyType type : CurrencyType.values()) {

                if (!data.hasBalance(type)) {

                    InsertDefaultBalanceAction insertAction = new InsertDefaultBalanceAction(uuid, type);

                    Boolean insertSuccess = false;
                    try {
                        insertSuccess = _dbConnector.executeActionSync(insertAction);
                    } catch (SQLException | DatabaseException e) {
                        e.printStackTrace();
                    }

                    if (insertSuccess)
                        data.putBalance(type, type.getDefaultBalance());

                }

            }

            future.complete(data);

        });

        dataFuture.exceptionally(ex -> {

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

}
