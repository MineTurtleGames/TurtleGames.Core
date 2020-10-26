package co.turtlegames.core.currency.action;

import co.turtlegames.core.currency.CurrencyData;
import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;

public class FetchPlayerCurrenciesAction implements IDatabaseAction<EnumMap<CurrencyType, Integer>> {

    private UUID _uuid;

    public FetchPlayerCurrenciesAction(UUID uuid) {
        _uuid = uuid;
    }

    @Override
    public EnumMap<CurrencyType, Integer> executeAction(Connection con) throws SQLException, DatabaseException {
        EnumMap<CurrencyType, Integer> currencies = new EnumMap<>(CurrencyType.class);

        PreparedStatement statement = con.prepareStatement("SELECT * FROM currency WHERE uuid=?");

        statement.setString(1, _uuid.toString());

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {

            currencies.put(CurrencyType.valueOf(resultSet.getString("type")), resultSet.getInt("balance"));

        }

        return currencies;
    }
}
