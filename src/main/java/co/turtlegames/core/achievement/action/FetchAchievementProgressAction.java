package co.turtlegames.core.achievement.action;

import co.turtlegames.core.achievement.AchievementCategory;
import co.turtlegames.core.achievement.MetaAchievement;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FetchAchievementProgressAction implements IDatabaseAction<Map<Integer, Integer>> {

    private UUID _targetUuid;

    public FetchAchievementProgressAction(UUID targetUuid) {
        _targetUuid = targetUuid;
    }

    @Override
    public Map<Integer, Integer> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement fetchStatement = con.prepareStatement("SELECT * FROM `achievement` WHERE `owner_uuid`=?");

        fetchStatement.setString(1, _targetUuid.toString());

        ResultSet rs = fetchStatement.executeQuery();
        Map<Integer, Integer> achievementProgress = new HashMap<>();

        while(rs.next()) {
            achievementProgress.put(rs.getInt("achievement_id"), rs.getInt("value"));
        }

        return achievementProgress;

    }

}
