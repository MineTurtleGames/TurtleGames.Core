package co.turtlegames.core.menu;

import org.bukkit.inventory.ItemStack;

public class Button {

    private int _slot;
    private ItemStack _item;
    private IButtonCallback _callback;

    public Button(int slot, ItemStack item, IButtonCallback callback) {

        _slot = slot;
        _item = item;
        _callback = callback;

    }

    public Button(int slot, ItemStack item) {
        this(slot, item, null);
    }

    public int getSlot() {
        return _slot;
    }

    public ItemStack getItem() {
        return _item;
    }

    public IButtonCallback getCallback() {
        return _callback;
    }
}
