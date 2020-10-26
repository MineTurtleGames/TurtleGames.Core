package co.turtlegames.core.currency.action;

import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class UpdatePlayerCurrencyAction implements IDatabaseAction<Boolean> {

    private UUID _uuid;
    private CurrencyType _type;
    private int _deltaCurrency;

    public UpdatePlayerCurrencyAction(UUID uuid, CurrencyType type, int deltaCurrency) {
        _uuid = uuid;
        _type = type;
        _deltaCurrency = deltaCurrency;
    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO currency (uuid, type, balance) VALUES (?,?,?) ON DUPLICATE KEY UPDATE balance=balance+?");

        statement.setString(1, _uuid.toString());
        statement.setString(2, _type.toString());
        statement.setInt(3, _deltaCurrency);
        statement.setInt(4, _deltaCurrency);

        int rowsAffected = statement.executeUpdate();

        return rowsAffected == 1;

    }

}
