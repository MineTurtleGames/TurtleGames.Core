package co.turtlegames.core.achievement;

import java.util.HashMap;
import java.util.UUID;

public class AchievementData {

    private AchievementManager _achievementManager;
    private UUID _ownerUuid;

    private HashMap<Integer, AchievementStatus> _achievementData;

    public AchievementData(AchievementManager achievementManager, UUID owner) {

        _achievementData = new HashMap<>();

        _achievementManager = achievementManager;
        _ownerUuid = owner;

    }

    protected AchievementManager getAchievementManager() {
        return _achievementManager;
    }

    protected void updateAchievementStatus(AchievementStatus status) {
        _achievementData.put(status.getType().getId(), status);
    }

    public AchievementStatus getAchievementStatus(MetaAchievement metaAchievement) {

        AchievementStatus status = _achievementData.get(metaAchievement.getId());

        if(status != null)
            return status;

        status = new AchievementStatus(this, metaAchievement, 0);
        _achievementData.put(metaAchievement.getId(), status);

        return status;

    }

    public UUID getOwnerUuid() {
        return _ownerUuid;
    }

}
