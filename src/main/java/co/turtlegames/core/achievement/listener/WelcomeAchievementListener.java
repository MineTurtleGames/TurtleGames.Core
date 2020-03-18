package co.turtlegames.core.achievement.listener;

import co.turtlegames.core.achievement.AchievementData;
import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.AchievementStatus;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeAchievementListener implements Listener {

    private AchievementManager _achievementManager;

    public WelcomeAchievementListener(AchievementManager achievementManager) {
        _achievementManager = achievementManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        ProfileManager profileManager = _achievementManager.getModule(ProfileManager.class);

        profileManager.fetchProfile(event.getPlayer().getUniqueId())
                .thenAccept((PlayerProfile profile) -> {

                    profile.fetchAchievementData().thenAccept((AchievementData data) -> {

                        AchievementStatus status = data.getAchievementStatus(_achievementManager.getAchievementById(1));

                        if(!status.isComplete())
                            status.incrementProgress(1);

                    });

        });

    }

}
