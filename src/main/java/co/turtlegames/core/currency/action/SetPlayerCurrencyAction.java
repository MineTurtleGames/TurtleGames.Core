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

        PreparedStatement statement = con.prepareStatement("UPDATE currency SET balance=? WHERE uuid=? AND currency_type=?");

        statement.setInt(1, _newBalance);
        statement.setString(2, _uuid.toString());
        statement.setString(3, _type.toString());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected == 1;

    }

}
