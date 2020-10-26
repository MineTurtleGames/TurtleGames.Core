package co.turtlegames.core.chat.dm;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand extends CommandBase<DirectMessageManager> {

    public ReplyCommand(DirectMessageManager manager) {
        super(manager, Rank.PLAYER, "reply", "r", "respond");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        if (args.length == 0) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "/r <Message>"));
            return;

        }

        UUID uuid = getModule().getLastMessageSender(profile.getOwnerUuid());

        if (uuid == null) {

            profile.getOwner().sendMessage(Chat.main(getModule().getName(), "There is nobody for you to reply to!"));
            return;

        }

        String message = StringUtils.join(args, ' ');

        Player ply = profile.getOwner();
        Player target = Bukkit.getPlayer(uuid);

        if (target == null) {

            ply.sendMessage(Chat.main(getModule().getName(), "The target player is no longer in the same server as you."));
            return;

        }

        String dmMessage = ChatColor.DARK_GREEN + "[" + ply.getName() + " > " + target.getName() + "] " + ChatColor.GRAY + message;
        getModule().pushLastMessageSender(target.getUniqueId(), ply.getUniqueId());

        target.sendMessage(dmMessage);
        ply.sendMessage(dmMessage);

    }

}
