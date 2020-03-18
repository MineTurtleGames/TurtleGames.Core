package co.turtlegames.core.achievement.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class IncrementAchievementProgressAction implements IDatabaseAction<Integer> {

    private int _metaId;
    private UUID _ownerUuid;

    private int _increaseAmount;

    public IncrementAchievementProgressAction(int metaId, UUID ownerUuid, int increaseAmount) {
        _metaId = metaId;
        _ownerUuid = ownerUuid;
        _increaseAmount = increaseAmount;
    }

    @Override
    public Integer executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement updateStatement = con.prepareStatement("UPDATE `achievement` SET `value`=`value`+? WHERE `achievement_id`=? AND `owner_uuid`=?");

        updateStatement.setInt(1, _increaseAmount);
        updateStatement.setInt(2, _metaId);

        updateStatement.setString(3, _ownerUuid.toString());

        updateStatement.execute();

        PreparedStatement fetchStatement = con.prepareStatement("SELECT `value` FROM `achievement` WHERE `achievement_id`=? AND `owner_uuid`=?");

        fetchStatement.setInt(1, _metaId);
        fetchStatement.setString(2, _ownerUuid.toString());

        ResultSet rs = fetchStatement.executeQuery();
        rs.next();

        return rs.getInt("value");

    }

}
