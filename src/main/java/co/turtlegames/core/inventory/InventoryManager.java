package co.turtlegames.core.inventory;

import co.turtlegames.core.TurtleModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class InventoryManager extends TurtleModule {

    private HashMap<String, Class<? extends AbstractItem>> _itemTypes = new HashMap<>();

    public InventoryManager(JavaPlugin plugin) {
        super(plugin, "Inventory");
    }

    @Override
    public void initializeModule() {

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

}
