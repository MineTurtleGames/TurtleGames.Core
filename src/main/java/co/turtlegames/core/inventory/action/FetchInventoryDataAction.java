package co.turtlegames.core.inventory.action;

import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.db.IDatabaseAction;
import co.turtlegames.core.inventory.AbstractItem;
import co.turtlegames.core.inventory.IItemMetaData;
import co.turtlegames.core.inventory.InventoryData;
import co.turtlegames.core.inventory.InventoryManager;
import com.google.common.collect.Multimap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FetchInventoryDataAction implements IDatabaseAction<InventoryData> {

    private InventoryManager _manager;
    private UUID _uuid;

    public FetchInventoryDataAction(InventoryManager manager, UUID uuid) {

        _manager = manager;
        _uuid = uuid;

    }

    @Override
    public InventoryData executeAction(Connection con) throws SQLException, DatabaseException {

        PreparedStatement statement = con.prepareStatement("SELECT * FROM inventory WHERE uuid=?");
        statement.setString(1, _uuid.toString());

        ResultSet rs = statement.executeQuery();

        ArrayList<AbstractItem> items = new ArrayList<>();

        while (rs.next()) {

            Class<? extends AbstractItem> clazz = _manager.getItemClass(rs.getString("type"));

            if (clazz == null)
                continue;

            try {

                Constructor<? extends AbstractItem> constructor = clazz.getConstructor(UUID.class);
                AbstractItem item = constructor.newInstance(_uuid);
                item.setId(rs.getInt("id"));

                if (item instanceof IItemMetaData) {

                    String dataString = rs.getString("data");
                    HashMap<String, String> data = new HashMap<>();

                    for (String element : dataString.split(";")) {

                        String[] parts = element.split("=");
                        data.put(parts[0], parts[1]);

                    }

                    ((IItemMetaData) item).loadFrom(data);

                }

                items.add(item);

            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        InventoryData data = new InventoryData(_manager, _uuid, items);

        return data;

    }
}
