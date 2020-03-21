package co.turtlegames.core.currency.action;

import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class InsertDefaultBalanceAction implements IDatabaseAction<Boolean> {

    private UUID _uuid;
    private CurrencyType _type;

    public InsertDefaultBalanceAction(UUID uuid, CurrencyType type) {
        _uuid = uuid;
        _type = type;
    }

    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("INSERT INTO currency (uuid, currency_type, balance) VALUES (?,?,?)");

        statement.setString(1, _uuid.toString());
        statement.setString(2, _type.toString());
        statement.setInt(3, _type.getDefaultBalance());

        int affectedRows = statement.executeUpdate();

        return affectedRows == 1;

    }

}
