package co.turtlegames.core.inventory.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.inventory.AbstractItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveItemAction implements IDatabaseAction<Boolean> {

    private AbstractItem _item;

    public RemoveItemAction(AbstractItem item) {
        _item = item;
    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("DELETE FROM inventory WHERE id=?");
        statement.setInt(1, _item.getId());

        return statement.executeUpdate() > 0;

    }
}
