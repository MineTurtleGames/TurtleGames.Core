package co.turtlegames.core.currency.action;

import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class SetPlayerCurrencyAction implements IDatabaseAction<Boolean> {

    private UUID _uuid;
    private CurrencyType _type;
    private int _newBalance;

    public SetPlayerCurrencyAction(UUID uuid, CurrencyType type, int newBalance) {
        _uuid = uuid;
        _type = type;
        _newBalance = newBalance;
    }

    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO currency (uuid, type balance) VALUES (?,?,?) ON DUPLICATE KEY UPDATE balance=?");

        statement.setString(1, _uuid.toString());
        statement.setString(2, _type.toString());
        statement.setInt(3, _newBalance);
        statement.setInt(4, _newBalance);

        int rowsAffected = statement.executeUpdate();

        return rowsAffected >= 1; // if 1, row inserted. if 2, row updated.

    }

}
