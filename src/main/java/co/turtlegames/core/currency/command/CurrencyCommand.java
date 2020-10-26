package co.turtlegames.core.currency.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyManager;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.CompletableFuture;

public class CurrencyCommand extends CommandBase<CurrencyManager> {

    public CurrencyCommand(CurrencyManager module) {
        super(module, Rank.ADMINISTRATOR, "currency");
    }

    public void executeCommand(PlayerProfile profile, String[] args) {

        if (args.length == 0) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "/currency set <Player> <Currency> <Amount>"));
            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "/currency modify <Player> <Currency> <Amount>"));
            return;

        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length != 4) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "/currency set <Player> <Currency> <Amount>"));
                return;

            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            CurrencyType type;

            try {
                type = CurrencyType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException ex) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Please enter a valid currency"));
                return;

            }

            int newBalance;

            try {
                newBalance = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Please enter a whole number"));
                return;

            }

            CompletableFuture<CurrencyData> dataFuture = profile.fetchCurrencyData();

            dataFuture.thenAccept(currencyData -> {

                CompletableFuture<Boolean> future = currencyData.setBalance(type, newBalance);
                future.thenAccept(success -> {

                    if (success) {

                        profile.getOwner().sendMessage(Chat.main(getModule().getName(), Chat.elem(target.getName()) + " balance is now " + Chat.elem(type.format(newBalance))));

                    }

                });

            });

        }

        if (args[0].equalsIgnoreCase("modify")) {

            if (args.length != 4) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "/currency modify <Player> <Currency> <Amount>"));
                return;

            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            CurrencyType type;

            try {
                type = CurrencyType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException ex) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Please enter a valid currency"));
                return;

            }

            int newBalance;

            try {
                newBalance = Integer.parseInt(args[3]);
            } catch (NumberFormatException ex) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Please enter a whole number"));
                return;

            }

            CompletableFuture<CurrencyData> dataFuture = profile.fetchCurrencyData();

            dataFuture.thenAccept(currencyData -> {

                CompletableFuture<Boolean> future = currencyData.updateBalance(type, newBalance);
                future.thenAccept(success -> {

                    if (success) {

                        profile.getOwner().sendMessage(Chat.main(getModule().getName(), Chat.elem(target.getName()) + " balance is now " + Chat.elem(type.format(newBalance))));

                    } else {

                        profile.getOwner().sendMessage(Chat.main(getModule().getName(), "A server error occurred whilst attempting to update the target player's balance."));

                    }

                });

            });

        }

    }

}
