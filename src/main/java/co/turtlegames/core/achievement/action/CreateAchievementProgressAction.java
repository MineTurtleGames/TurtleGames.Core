package co.turtlegames.core.achievement.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CreateAchievementProgressAction implements IDatabaseAction<Integer> {

    private int _metaId;
    private UUID _ownerUuid;

    private int _increaseAmount;

    public CreateAchievementProgressAction(int metaId, UUID ownerUuid, int increaseAmount) {
        _metaId = metaId;
        _ownerUuid = ownerUuid;
        _increaseAmount = increaseAmount;
    }

    @Override
    public Integer executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO `achievement` (achievement_id, owner_uuid, value) VALUES (?, ?, ?)");

        statement.setInt(1, _metaId);
        statement.setString(2, _ownerUuid.toString());
        statement.setInt(3, _increaseAmount);

        statement.execute();

        return _increaseAmount;

    }

}
