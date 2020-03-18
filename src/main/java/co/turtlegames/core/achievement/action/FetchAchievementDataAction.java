package co.turtlegames.core.achievement.action;

import co.turtlegames.core.achievement.AchievementCategory;
import co.turtlegames.core.achievement.MetaAchievement;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import com.google.common.collect.ArrayListMultimap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FetchAchievementDataAction implements IDatabaseAction<Collection<MetaAchievement>> {

    @Override
    public Collection<MetaAchievement> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement fetchStatement = con.prepareStatement("SELECT * FROM `achievement_type`");

        ResultSet rs = fetchStatement.executeQuery();
        List<MetaAchievement> achievements = new ArrayList<>();

        while(rs.next()) {

            achievements.add(new MetaAchievement(rs.getInt("id"),
                                                    rs.getString("name"),
                                                    rs.getString("description"),
                                                    AchievementCategory.valueOf(rs.getString("game")),
                                                    rs.getInt("reward_xp"),
                                                    rs.getInt("reward_coins"),
                                                    rs.getInt("goal_value")));

        }

        return achievements;

    }

}
