package co.turtlegames.core.infraction.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.*;

public class RegisterInfractionAction implements IDatabaseAction<Integer> {

    private Infraction _toRegister;

    public RegisterInfractionAction(Infraction toRegister) {
        _toRegister = toRegister;
    }

    @Override
    public Integer executeAction(Connection con) throws SQLException {

        PreparedStatement statement =
                con.prepareStatement("INSERT INTO `infraction` (`owner_uuid`, `issuer_uuid`, `type`, `issue_time`, `length`, `reason`)"
                        + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, _toRegister.getOwner().toString());
        statement.setString(2, _toRegister.getIssuer().toString());
        statement.setString(3, _toRegister.getType().toString());

        statement.setLong(4, _toRegister.getIssueEpoch());
        statement.setLong(5, _toRegister.getDuration());

        statement.setString(6, _toRegister.getReason());

        statement.execute();
        ResultSet resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {

            return resultSet.getInt(1);

        }

        return -1;

    }

}
