package co.turtlegames.core.profile.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.profile.Rank;
import com.avaje.ebean.Update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UpdateProfileRankAction implements IDatabaseAction<Boolean> {

    private UUID _targetUuid;
    private Rank _newRank;

    public UpdateProfileRankAction(UUID uuid, Rank newRank) {

        _targetUuid = uuid;
        _newRank = newRank;

    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("UPDATE `profile` SET `rank`=? WHERE `uuid`=?");

        statement.setString(1, _newRank.toString());
        statement.setString(2, _targetUuid.toString());

        return statement.executeUpdate() > 0;

    }

}
