package co.turtlegames.core.achievement.unique;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.profile.PlayerProfile;
import org.bukkit.entity.Player;

public interface IAchievementUniqueReward {

    public void registerManager(AchievementManager achievementManager);
    public String getRewardLine();

    public void grantReward(PlayerProfile profile);

}
