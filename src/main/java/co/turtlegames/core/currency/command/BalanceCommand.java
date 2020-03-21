package co.turtlegames.core.currency.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyManager;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;

import java.util.concurrent.CompletableFuture;

public class BalanceCommand extends CommandBase<CurrencyManager> {

    public BalanceCommand(CurrencyManager module) {
        super(module, Rank.PLAYER, "balance", "bal");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        if (args.length != 1) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Usage: /balance <Currency>"));
            return;

        }

        CurrencyType type;

        try {
            type = CurrencyType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException ex) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "There is no currency with that name"));
            return;

        }

        CompletableFuture<CurrencyData> future = profile.fetchCurrencyData();

        future.thenAccept(currencyData -> {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Balance: " + Chat.elem(type.format(currencyData.getBalance(type)))));

        });

        future.exceptionally(ex -> {

            ex.printStackTrace();
            return null;

        });

    }

}
