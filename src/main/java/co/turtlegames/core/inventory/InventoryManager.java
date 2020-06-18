package co.turtlegames.core.inventory;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.inventory.action.FetchInventoryDataAction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class InventoryManager extends TurtleModule {

    private DatabaseConnector _dbConnector;
    private HashMap<String, Class<? extends AbstractItem>> _itemTypes = new HashMap<>();

    public InventoryManager(JavaPlugin plugin) {
        super(plugin, "Inventory");
    }

    @Override
    public void initializeModule() {
        _dbConnector = getDatabaseConnector();
    }

    public void registerItem(String type, Class<? extends AbstractItem> clazz) {
        _itemTypes.put(type, clazz);
    }

    public Set<String> getAllTypes() {
        return _itemTypes.keySet();
    }

    public Class<? extends AbstractItem> getItemClass(String type) {

        if (!_itemTypes.containsKey(type))
            return null;

        return _itemTypes.get(type);

    }

    public CompletableFuture<InventoryData> fetchInventoryData(UUID uuid) {

        CompletableFuture<InventoryData> future = new CompletableFuture<>();

        FetchInventoryDataAction action = new FetchInventoryDataAction(this, uuid);
        CompletableFuture<InventoryData> dbFuture = _dbConnector.executeActionAsync(action);

        dbFuture.thenAccept(future::complete);

        dbFuture.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

    public String validateMetaData(Object target) {
        return target.toString().replaceAll(";", "").replaceAll("=", "");
    }

    public String serializeMetaData(AbstractItem item) {

        if (!(item instanceof IItemMetaData))
            return "";

        IItemMetaData itemMetaData = (IItemMetaData) item;

        Map<String, Object> serialized = itemMetaData.serialize();
        ArrayList<String> elements = new ArrayList<>();

        for (String key : serialized.keySet()) {
            elements.add(validateMetaData(key) + "=" + validateMetaData(serialized.get(key)));
        }

        return StringUtils.join(elements, ";");

    }

}
