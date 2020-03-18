package co.turtlegames.core.infraction.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.infraction.menu.InfractionMenu;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PunishCommand extends CommandBase<InfractionManager> {

    public PunishCommand(InfractionManager module) {
        super(module, Rank.JNR_MODERATOR, "punish", "p");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player player = profile.getOwner();

        if (args.length < 2) {

            player.sendMessage(Chat.main(getModule().getName(), "Usage: /punish <Name> <Reason>"));
            return;

        }

        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');

        InfractionMenu menu = new InfractionMenu(getModule(), player, target, reason);
        menu.open();

    }

}
