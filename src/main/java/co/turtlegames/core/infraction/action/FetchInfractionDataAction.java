package co.turtlegames.core.infraction.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.infraction.InfractionType;
import co.turtlegames.core.db.IDatabaseAction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FetchInfractionDataAction implements IDatabaseAction<Collection<Infraction>> {

    private UUID _targetUuid;

    public FetchInfractionDataAction(UUID targetUuid) {
        _targetUuid = targetUuid;
    }

    @Override
    public Collection<Infraction> executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM `infraction` WHERE `owner_uuid`=? ORDER BY `issue_time` DESC");
        preparedStatement.setString(1, _targetUuid.toString());

        ResultSet rs = preparedStatement.executeQuery();
        List<Infraction> infractions = new ArrayList<>();

        while(rs.next()) {

            infractions.add(new Infraction(UUID.fromString(rs.getString("owner_uuid")),
                    UUID.fromString(rs.getString("issuer_uuid")),
                    InfractionType.valueOf(rs.getString("type")),
                    rs.getLong("issue_time"),
                    rs.getLong("length"), rs.getString("reason")));

        }

        return infractions;

    }

}
