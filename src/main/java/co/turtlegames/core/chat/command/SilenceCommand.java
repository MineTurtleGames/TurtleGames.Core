package co.turtlegames.core.chat.command;

import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.util.UtilString;

public class SilenceCommand extends CommandBase<ChatManager> {

    public SilenceCommand(ChatManager module) {
        super(module, Rank.ADMINISTRATOR, "silence");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        getModule().setSilenced(!getModule().isSilenced());

        profile.getOwner().sendMessage(Chat.main(getModule().getName(), "Chat silence: " + UtilString.formatBoolean(getModule().isSilenced())));

    }

}
