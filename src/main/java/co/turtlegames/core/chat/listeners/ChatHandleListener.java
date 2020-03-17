package co.turtlegames.core.chat.listeners;

import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.chat.CoreChatEvent;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
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
            event.setCancelled(true);
            return;

        }

        CompletableFuture<PlayerProfile> profileFuture = _profileManager.fetchProfile(ply.getUniqueId());
        PlayerProfile profile = null;

        try {
            profile = profileFuture.get();
        } catch (InterruptedException | ExecutionException ex) {
            return;
        }

        if(profile == null)
            return;

        CoreChatEvent chatEvent = new CoreChatEvent(profile, event.getMessage());
        Bukkit.getPluginManager().callEvent(chatEvent);

        Bukkit.broadcastMessage(Chat.chatMessage(chatEvent, event.getMessage()));

    }

}
