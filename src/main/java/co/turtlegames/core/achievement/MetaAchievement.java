package co.turtlegames.core.achievement;

import org.bukkit.Achievement;

public class MetaAchievement {

    private int _id;

    private String _name;
    private String _description;

    private AchievementCategory _game;

    private int _rewardXp;
    private int _rewardCoins;

    private int _goalValue;

    public MetaAchievement(int id, String name, String description, AchievementCategory game, int rewardXp, int rewardCoins, int goalValue) {
        _id = id;

        _name = name;
        _description = description;

        _game = game;

        _rewardXp = rewardXp;
        _rewardCoins = rewardCoins;

        _goalValue = goalValue;
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
}
