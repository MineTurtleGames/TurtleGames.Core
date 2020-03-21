package co.turtlegames.core.stats.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class IncrementStatAction implements IDatabaseAction<Double> {

    private UUID _owner;

    private String _game;
    private String _name;

    private double _amount;

    public IncrementStatAction(UUID owner, String game, String name, double amount) {

        _owner = owner;

        _game = game;
        _name = name;

        _amount = amount;

    }


    @Override
    public Double executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement updateStatement = con.prepareStatement("INSERT INTO `stat` (`owner_uuid`, `game`, `stat`, `value`) VALUES (?, ?, ?, ?)" +
                                                                        "ON DUPLICATE KEY UPDATE `value`=`value`+?");

        updateStatement.setString(1, _owner.toString());
        updateStatement.setString(2, _game);
        updateStatement.setString(3, _name);
        updateStatement.setDouble(4, _amount);

        updateStatement.setDouble(5, _amount);

        updateStatement.executeUpdate();

        PreparedStatement fetchNewValueStatement = con.prepareStatement("SELECT `value` FROM `stat` WHERE `owner_uuid`=? AND `game`=? AND `stat`=?");
        fetchNewValueStatement.setString(1, _owner.toString());
        fetchNewValueStatement.setString(2,  _game);
        fetchNewValueStatement.setString(3, _name);

        ResultSet rs = fetchNewValueStatement.executeQuery();
        rs.next();

        return rs.getDouble("value");

    }

}
