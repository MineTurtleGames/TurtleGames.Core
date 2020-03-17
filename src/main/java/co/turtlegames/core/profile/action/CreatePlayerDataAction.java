package co.turtlegames.core.profile.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.*;
import java.util.UUID;

public class CreatePlayerDataAction implements IDatabaseAction<Boolean> {

    private UUID _targetUuid;

    public CreatePlayerDataAction(UUID targetUuid) {
        _targetUuid = targetUuid;
    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO `profile` (`uuid`) VALUES (?)");
        statement.setString(1, _targetUuid.toString());

        statement.executeUpdate();

        return true;
    }

}
