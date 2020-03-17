package co.turtlegames.core.profile.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class FetchPlayerDataAction implements IDatabaseAction<FetchPlayerDataAction.PlayerData> {

    public class PlayerData {

        private HashMap<String, Object> data = new HashMap<>();

        public HashMap<String, Object>getData() {
            return data;
        }

    }

    private UUID _targetUuid;

    public FetchPlayerDataAction(UUID uuid) {
        _targetUuid = uuid;
    }

    @Override
    public PlayerData executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("SELECT * FROM `profile` WHERE `uuid`=?");
        statement.setString(1, _targetUuid.toString());

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next())
            throw new DatabaseException(DatabaseException.FailureType.NOT_FOUND, "Failed to find player data");

        PlayerData playerData = new PlayerData();

        ResultSetMetaData resultMeta = resultSet.getMetaData();
        int columns = resultMeta.getColumnCount();

        for (int i = 1; i <= columns; i++)
            playerData.getData().put(resultMeta.getColumnName(i), resultSet.getObject(i));

        return playerData;

       }

}
