package co.turtlegames.core.inventory.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.inventory.AbstractItem;
import co.turtlegames.core.inventory.InventoryManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateItemAction implements IDatabaseAction<Boolean> {

    private InventoryManager _manager;
    private AbstractItem _item;

    public UpdateItemAction(InventoryManager manager, AbstractItem item) {

        _manager = manager;
        _item = item;

    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        String data = _manager.serializeMetaData(_item);

        PreparedStatement statement = con.prepareStatement("UPDATE inventory SET owner_uuid=?,data=? WHERE id=?");
        statement.setString(1, _item.getOwner().toString());
        statement.setString(2, data);
        statement.setInt(3, _item.getId());

        return statement.executeUpdate() > 0;

    }

}
