package co.turtlegames.core.chat.listeners;

import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.chat.CoreChatEvent;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.infraction.InfractionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ChatFilterListener implements Listener {

    private ChatManager _chatManager;
    private ProfileManager _profileManager;

    public ChatFilterListener(ChatManager chatManager) {

        _chatManager = chatManager;
        _profileManager = _chatManager.getModule(ProfileManager.class);

    }

    @EventHandler
    public void onChat(CoreChatEvent ev) {

        if(ev.getMessage().contains("gary")) {

            _chatManager.getModule(InfractionManager.class)
                    .registerInfraction(new Infraction(ev.getSender().getOwnerUuid(), UUID.randomUUID(), InfractionType.WARN, System.currentTimeMillis(), 1000));

        }

    }

}
