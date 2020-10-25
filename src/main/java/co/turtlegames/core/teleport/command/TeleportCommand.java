package co.turtlegames.core.teleport.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.teleport.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;

public class TeleportCommand extends CommandBase<TeleportManager> {

    public TeleportCommand(TeleportManager manager) {
        super(manager, Rank.ADMINISTRATOR, "teleport", "tp");
    }

    public void showHelp(Player player) {

        player.sendMessage(new String[]{
                ChatColor.DARK_GREEN + "/tp <Player> - Teleport to a player",
                ChatColor.DARK_GREEN + "/tp here <Player> - Teleport a player to you",
                ChatColor.DARK_GREEN + "/tp all - Teleport every player to you",
                ChatColor.DARK_GREEN + "/tp <Player1> <Player2> - Teleport Player1 to Player2"
        });

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        if (args.length == 0) {

            showHelp(profile.getOwner());
            return;

        }

        String target = args[0];

        if (target.equalsIgnoreCase("here")) {

            target = args[1];

            Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Unable to find player with username " + Chat.elem(target) + "."));
                return;

            }

            targetPlayer.teleport(profile.getOwner());
            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "You teleported to " + Chat.elem(targetPlayer.getName()) + "."));
            return;

        }

        if (target.equalsIgnoreCase("all")) {

            if (!profile.getRank().isPermissible(Rank.OWNER)) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "You need rank " + Rank.OWNER.getColor() + "Owner" + Chat.body(" to use that command")));
                return;

            }

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player != profile.getOwner()) {

                    player.teleport(profile.getOwner());
                    player.sendMessage(Chat.main(getModule().getName(), "You were teleported to " + Chat.elem(profile.getOwner().getName()) + "."));

                }

            }

            return;

        }

        if (args.length == 1) {

            Player targetPlayer = Bukkit.getPlayer(target);

            if (targetPlayer == null) {

                profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Unable to find player with username " + Chat.elem(target) + "."));
                return;

            }

            profile.getOwner().teleport(targetPlayer);
            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "You teleported to " + Chat.elem(targetPlayer.getName()) + "."));
            return;

        }

        if (args.length != 2) {

            showHelp(profile.getOwner());
            return;

        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        Player toPlayer = Bukkit.getPlayer(args[1]);

        if (targetPlayer == null || toPlayer == null) {

            String username = targetPlayer == null ? args[0] : args[1];
            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Unable to find player with username " + Chat.elem(username) + "."));
            return;

        }

        targetPlayer.teleport(toPlayer);
        targetPlayer.sendMessage(Chat.main(getModule().getName(), "You teleported to " + Chat.elem(toPlayer.getName()) + "."));
        toPlayer.sendMessage(Chat.main(getModule().getName(), Chat.elem(toPlayer.getName()) + " was teleported to you by an admin."));

        profile.getOwner().sendMessage(Chat.main(getModule().getName(), Chat.elem(targetPlayer.getName()) + " was teleported to " + Chat.elem(toPlayer.getName()) + "."));
    }

}
