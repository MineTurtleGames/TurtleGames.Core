package co.turtlegames.core.stats.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.stats.PlayerStat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FetchStatDataAction implements IDatabaseAction<Collection<PlayerStat>> {

    private UUID _target;

    public FetchStatDataAction(UUID target) {
        _target = target;
    }

    @Override
    public Collection<PlayerStat> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement fetchStatement = con.prepareStatement("SELECT * FROM `stat` WHERE `owner_uuid`=?");
        fetchStatement.setString(1, _target.toString());

        ResultSet rs = fetchStatement.executeQuery();
        List<PlayerStat> stats = new ArrayList<>();

        while(rs.next()) {
            stats.add(new PlayerStat(rs.getString("game"),
                            rs.getString("stat"),
                            rs.getDouble("value")));
        }

        return stats;

    }

}
