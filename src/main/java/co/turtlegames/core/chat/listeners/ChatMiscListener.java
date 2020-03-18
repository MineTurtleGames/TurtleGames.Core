package co.turtlegames.core.chat.listeners;

import co.turtlegames.core.achievement.AchievementData;
import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.AchievementStatus;
import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import org.bukkit.Achievement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.ExecutionException;

public class ChatMiscListener implements Listener {

    private ChatManager _chatManager;

    public ChatMiscListener(ChatManager chatManager) {
        _chatManager = chatManager;
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent ev) {

        if(!ev.getMessage().contains("gary"))
            return;

        ProfileManager manager = _chatManager.getModule(ProfileManager.class);
        AchievementManager achievementManager = _chatManager.getModule(AchievementManager.class);

        AchievementData achievementData;
        try {

            PlayerProfile profile = manager.fetchProfile(ev.getPlayer().getUniqueId()).get();
            achievementData = profile.fetchAchievementData().get();

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return;
        }

        AchievementStatus status = achievementData.getAchievementStatus(achievementManager.getAchievementById(2));

        if(!status.isComplete())
            status.incrementProgress(1);

    }

}
