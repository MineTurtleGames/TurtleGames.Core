package co.turtlegames.core.infraction.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterInfractionAction implements IDatabaseAction<Boolean> {

    private Infraction _toRegister;

    public RegisterInfractionAction(Infraction toRegister) {
        _toRegister = toRegister;
    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement =
                con.prepareStatement("INSERT INTO `infraction` (`owner_uuid`, `issuer_uuid`, `type`, `issue_time`, `length`, `reason`)"
                        + " VALUES (?, ?, ?, ?, ?, ?)");

        statement.setString(1, _toRegister.getOwner().toString());
        statement.setString(2, _toRegister.getIssuer().toString());
        statement.setString(3, _toRegister.getType().toString());

        statement.setLong(4, _toRegister.getIssueEpoch());
        statement.setLong(5, _toRegister.getDuration());

        statement.setString(6, _toRegister.getReason());

        statement.execute();
        return true;

    }

}
