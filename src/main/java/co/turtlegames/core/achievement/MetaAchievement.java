package co.turtlegames.core.achievement;

import co.turtlegames.core.achievement.menu.MetaAchievementFlag;
import co.turtlegames.core.achievement.unique.IAchievementUniqueReward;
import org.bukkit.Achievement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetaAchievement {

    private AchievementManager _achievementManager;
    private int _id;

    private String _name;
    private String _description;

    private AchievementCategory _game;

    private int _rewardXp;
    private int _rewardCoins;

    private int _goalValue;

    private Map<AchievementFlagType, MetaAchievementFlag> _flags;

    public MetaAchievement(AchievementManager achievementManager, int id, String name, String description, AchievementCategory game, int rewardXp, int rewardCoins, int goalValue, Collection<MetaAchievementFlag> flags) {

        _achievementManager = achievementManager;
        _id = id;

        _name = name;
        _description = description;

        _game = game;

        _rewardXp = rewardXp;
        _rewardCoins = rewardCoins;

        _goalValue = goalValue;

        _flags = new HashMap<>();

        for(MetaAchievementFlag flag : flags) {
            _flags.put(flag.getType(), flag);
        }

        if(this.hasFlag(AchievementFlagType.UNIQUE_REWARD)) {

            IAchievementUniqueReward reward = this.getFlagData(AchievementFlagType.UNIQUE_REWARD);

            if(reward == null)
                return;

            reward.registerManager(achievementManager);

        }

    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public AchievementCategory getGame() {
        return _game;
    }

    public int getRewardXp() {
        return _rewardXp;
    }

    public int getRewardCoins() {
        return _rewardCoins;
    }

    public int getGoalValue() {
        return _goalValue;
    }

    public String getDescription() {
        return _description;
    }

    public boolean hasFlag(AchievementFlagType type) {
        return _flags.containsKey(type);
    }

    public <I> I getFlagData(AchievementFlagType type) {

        if(!_flags.containsKey(type))
            return null;

        return _flags.get(type).getData();

    }

}
