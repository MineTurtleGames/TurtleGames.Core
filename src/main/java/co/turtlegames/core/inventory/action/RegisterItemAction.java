package co.turtlegames.core.inventory.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.inventory.AbstractItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class RegisterItemAction implements IDatabaseAction<Boolean> {

    private String _type;
    private UUID _owner;

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {



        return null;

    }

}
