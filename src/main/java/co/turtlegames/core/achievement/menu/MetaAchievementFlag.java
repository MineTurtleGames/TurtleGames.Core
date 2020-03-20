package co.turtlegames.core.achievement.menu;

import co.turtlegames.core.achievement.AchievementFlagType;

public class MetaAchievementFlag {

    private AchievementFlagType _type;
    private Object _data;

    public MetaAchievementFlag(AchievementFlagType type, Object data) {
        _type = type;
        _data = data;
    }

    public AchievementFlagType getType() {
        return _type;
    }

    public <I> I getData() {
        return (I)  _data;
    }

}
