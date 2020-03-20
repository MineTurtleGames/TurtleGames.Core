package co.turtlegames.core.achievement;

import co.turtlegames.core.achievement.action.CreateAchievementProgressAction;
import co.turtlegames.core.achievement.action.IncrementAchievementProgressAction;
import co.turtlegames.core.achievement.unique.IAchievementUniqueReward;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import io.netty.util.concurrent.CompleteFuture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AchievementStatus {

    private AchievementData _data;

    private MetaAchievement _achievementType;
    private int _value;

    private boolean _dbExists = false;

    public AchievementStatus(AchievementData data, MetaAchievement metaAchievement, int currentValue) {

        _data = data;

        _achievementType = metaAchievement;
        _value = currentValue;

        _dbExists = currentValue > 0;

    }

    public void incrementProgress(int amount) {

        IDatabaseAction<Integer> dbQuery;

        if(_dbExists) {
            dbQuery = new IncrementAchievementProgressAction(_achievementType.getId(), _data.getOwnerUuid(), amount);
        } else {
            dbQuery = new CreateAchievementProgressAction(_achievementType.getId(), _data.getOwnerUuid(), amount);
        }

        CompletableFuture<Integer> dbQueryFuture = _data.getAchievementManager().getDatabaseConnector().executeActionAsync(dbQuery);

        dbQueryFuture.thenAccept((Integer value) -> {

            _value = value;

            if(_value >= _achievementType.getGoalValue())
                this.doGoalCompletion();
            else
                this.doGoalProgression();

        });

        dbQueryFuture.exceptionally((Throwable ex) -> {

            Player ply = Bukkit.getPlayer(_data.getOwnerUuid());

            if(ply == null)
                return null;

            ply.sendMessage(Chat.main("Achievement", "Failed to update your achievement data"));
            return null;

        });

    }

    private void doGoalProgression() {
    }

    private void doGoalCompletion() {

        ProfileManager profileManager = _data.getAchievementManager().getModule(ProfileManager.class);

        profileManager.fetchProfile(_data.getOwnerUuid()).thenAccept((PlayerProfile profile) -> {
           profile.addXp(_achievementType.getRewardXp());
        });

        Player ply = Bukkit.getPlayer(_data.getOwnerUuid());

        if(ply == null)
            return;

        ply.sendMessage("\n    " + ChatColor.DARK_GREEN + ChatColor.BOLD + "ACHIEVEMENT COMPLETE!");
        ply.sendMessage("    " + ChatColor.GREEN + _achievementType.getName());

        ply.sendMessage("\n    " + ChatColor.GRAY + _achievementType.getDescription());
        ply.sendMessage("");

        ply.playSound(ply.getLocation(), Sound.ORB_PICKUP, 1, 0);

        if(_achievementType.hasFlag(AchievementFlagType.UNIQUE_REWARD)) {

            IAchievementUniqueReward reward = _achievementType.getFlagData(AchievementFlagType.UNIQUE_REWARD);

            if(reward == null) {
                ply.sendMessage(Chat.main("Achievement", "Failed to grant rewards. Please try again later"));
                return;
            }

            profileManager.fetchProfile(_data.getOwnerUuid())
                    .thenAccept(reward::grantReward);

        }

    }

    public MetaAchievement getType() {
        return _achievementType;
    }

    public boolean isComplete() {
        return _value >= this.getType().getGoalValue();
    }

    public int getProgress() {
        return _value;
    }

}
