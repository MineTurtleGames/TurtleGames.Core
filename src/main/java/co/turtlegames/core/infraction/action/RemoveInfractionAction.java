package co.turtlegames.core.infraction.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class RemoveInfractionAction implements IDatabaseAction<Boolean> {

    private int _id;
    private UUID _admin;
    private String _reason;

    public RemoveInfractionAction(int id, UUID admin, String reason) {

        _id = id;
        _admin = admin;
        _reason = reason;

    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException {
        PreparedStatement statement = con.prepareStatement("UPDATE `infraction` SET `removed`=1,`removed_by`=?,`removed_reason`=? WHERE `id`=?");

        statement.setString(1, _admin.toString());
        statement.setString(2, _reason);

        statement.setInt(3, _id);

        statement.execute();
        return true;

    }
}
