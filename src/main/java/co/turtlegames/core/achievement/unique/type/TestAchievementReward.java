package co.turtlegames.core.achievement.unique.type;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.unique.IAchievementUniqueReward;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.infraction.InfractionType;
import co.turtlegames.core.profile.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TestAchievementReward implements IAchievementUniqueReward {

    private AchievementManager _achievementManager;

    @Override
    public void registerManager(AchievementManager achievementManager) {
        _achievementManager = achievementManager;
    }

    @Override
    public String getRewardLine() {
        return ChatColor.LIGHT_PURPLE + "+ Hidden Unique Reward";
    }

    @Override
    public void grantReward(PlayerProfile profile) {

        _achievementManager.getModule(InfractionManager.class).registerInfraction(
                new Infraction(profile.getOwnerUuid(), UUID.randomUUID(), InfractionType.WARN, System.currentTimeMillis(), 0, "Go outside"));

    }

}
