package co.turtlegames.core.profile.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AddXpAction implements IDatabaseAction<Long> {

    private UUID _targetUuid;
    private long _amountToAdd;

    public AddXpAction(UUID targetUuid, long amountToAdd) {
        _targetUuid = targetUuid;
        _amountToAdd = amountToAdd;
    }

    @Override
    public Long executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("UPDATE `profile` SET `xp`=`xp`+? WHERE `uuid`=?");

        statement.setLong(1, _amountToAdd);
        statement.setString(2, _targetUuid.toString());

        statement.execute();

        PreparedStatement select = con.prepareStatement("SELECT xp FROM `profile` WHERE `uuid`=?");
        select.setString(1, _targetUuid.toString());

        ResultSet rs = select.executeQuery();
        rs.next();

        return rs.getLong("xp");

    }

}
