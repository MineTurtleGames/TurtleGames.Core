package co.turtlegames.core.achievement.action;

import co.turtlegames.core.achievement.AchievementCategory;
import co.turtlegames.core.achievement.AchievementFlagType;
import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.MetaAchievement;
import co.turtlegames.core.achievement.menu.MetaAchievementFlag;
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

    private AchievementManager _achievementManager;

    public FetchAchievementDataAction(AchievementManager achievementManager) {
        _achievementManager = achievementManager;
    }

    @Override
    public Collection<MetaAchievement> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement fetchStatement = con.prepareStatement("SELECT * FROM `achievement_type`");

        ResultSet rs = fetchStatement.executeQuery();
        List<MetaAchievement> achievements = new ArrayList<>();

        while(rs.next()) {

            PreparedStatement fetchFlagStatement = con.prepareStatement("SELECT * FROM `achievement_type_flag` WHERE `achievement_id`=?");
            fetchFlagStatement.setInt(1, rs.getInt("id"));

            ResultSet flagRs = fetchFlagStatement.executeQuery();
            Collection<MetaAchievementFlag> achievementFlags = new ArrayList<>();

            while(flagRs.next()) {

                AchievementFlagType type = AchievementFlagType.valueOf(flagRs.getString("flag_name"));
                achievementFlags.add(new MetaAchievementFlag(type, type.convertData(flagRs.getString("data"))));

            }

            achievements.add(new MetaAchievement(_achievementManager,
                                                    rs.getInt("id"),
                                                    rs.getString("name"),
                                                    rs.getString("description"),
                                                    AchievementCategory.valueOf(rs.getString("game")),
                                                    rs.getInt("reward_xp"),
                                                    rs.getInt("reward_coins"),
                                                    rs.getInt("goal_value"),
                                                        achievementFlags));

        }

        return achievements;

    }

}
