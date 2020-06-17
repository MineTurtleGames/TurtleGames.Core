package co.turtlegames.core.inventory;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.Collection;
import java.util.UUID;

public class InventoryData {

    private InventoryManager _manager;
    private UUID _uuid;
    private Multimap<String, AbstractItem> _items;

    public InventoryData(InventoryManager manager, UUID uuid, Collection<AbstractItem> items) {

        _manager = manager;
        _uuid = uuid;
        _items = MultimapBuilder.hashKeys().arrayListValues().build();

        items.forEach(item -> _items.put(item.getType(), item));

    }

    public InventoryManager getManager() {
        return _manager;
    }

    public UUID getUuid() {
        return _uuid;
    }

    public Multimap<String, AbstractItem> getItems() {
        return _items;
    }
}
