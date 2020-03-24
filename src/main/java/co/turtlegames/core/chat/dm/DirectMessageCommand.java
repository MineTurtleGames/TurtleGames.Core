package co.turtlegames.core.chat.dm;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.infraction.InfractionData;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DirectMessageCommand extends CommandBase<DirectMessageManager> {

    public DirectMessageCommand(DirectMessageManager directMessageManager) {
        super(directMessageManager, Rank.PLAYER, "dm", "pm", "msg", "message");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        if(args.length < 2) {

            ply.sendMessage(Chat.main("Message", "Invalid arguments! Refer to: /dm <name> <message..>"));
            return;

        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {

            ply.sendMessage(Chat.main("Message", "'"+ Chat.elem(args[0]) + "' is not online"));
            return;

        }

        String dmMessage = ChatColor.DARK_GREEN + "[" + ply.getName() + " > " + target.getName() + "]" + ChatColor.GRAY;

        boolean first = true;
        for(int i = 1; i < args.length; i++)
            dmMessage += " " + args[i];

        ply.sendMessage(dmMessage);
        target.sendMessage(dmMessage);

        ply.playSound(ply.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
        target.playSound(ply.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);

    }

}
