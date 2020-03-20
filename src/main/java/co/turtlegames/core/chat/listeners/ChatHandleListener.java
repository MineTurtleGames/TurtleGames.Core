package co.turtlegames.core.chat.listeners;

import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.chat.CoreChatEvent;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.infraction.InfractionType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.util.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ChatHandleListener implements Listener {

    private ChatManager _chatManager;
    private ProfileManager _profileManager;

    public ChatHandleListener(ChatManager chatManager) {

        _chatManager = chatManager;
        _profileManager = _chatManager.getModule(ProfileManager.class);

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        event.setCancelled(true);

        Player ply = event.getPlayer();

        if (_chatManager.isSilenced()) {

            ply.sendMessage(Chat.main(_chatManager.getName(), "The chat is currently silenced"));
            return;

        }

        CompletableFuture<PlayerProfile> profileFuture = _profileManager.fetchProfile(ply.getUniqueId());
        PlayerProfile profile = null;

        try {
            profile = profileFuture.get();
            profile.fetchInfractionData().get();
        } catch (InterruptedException | ExecutionException ex) {
            return;
        }

        Infraction activeMute = profile.getInfractionData()
                                    .getRelevantInfraction(InfractionType.MUTE);

        if(activeMute != null) {
            
            ply.sendMessage(Chat.main("Mute", "You are muted for " + Chat.elem(UtilString.formatTime(activeMute.getMsUntilExpiry()))
                    + "\nReason: " + activeMute.getReason()));

            return;

        }

        CoreChatEvent chatEvent = new CoreChatEvent(profile, event.getMessage());
        Bukkit.getPluginManager().callEvent(chatEvent);

        Bukkit.broadcastMessage(Chat.chatMessage(chatEvent, event.getMessage()));

    }

}
