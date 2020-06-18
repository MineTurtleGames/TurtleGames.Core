package co.turtlegames.core.inventory.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.inventory.AbstractItem;
import co.turtlegames.core.inventory.IItemMetaData;
import co.turtlegames.core.inventory.InventoryManager;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class RegisterItemAction implements IDatabaseAction<Boolean> {

    private InventoryManager _manager;
    private AbstractItem _item;

    public RegisterItemAction(InventoryManager manager, AbstractItem item) {

        _manager = manager;
        _item = item;

    }

    @Override
    public Boolean executeAction(Connection con) throws SQLException, DatabaseException {

        String data = _manager.serializeMetaData(_item);

        PreparedStatement statement = con.prepareStatement("INSERT INTO inventory (owner_uuid, item, data) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, _item.getOwner().toString());
        statement.setString(2, _item.getType());
        statement.setString(3, data);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {

            _item.setId(rs.getInt(1));
            return true;

        }

        return false;

    }

}
