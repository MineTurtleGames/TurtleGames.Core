package co.turtlegames.core.menu.predefined;

import co.turtlegames.core.menu.IButtonCallback;
import co.turtlegames.core.menu.Menu;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmPageBase<MenuType extends Menu> extends Page {

    public interface ConfirmPageCallback {

        void confirm(Player player);
        void cancel(Player player);

    }

    private ItemStack _confirmOption;
    private ConfirmPageCallback _callback;

    public ConfirmPageBase(MenuType menu, int rows) {
        super(menu, "Confirm", 6);
    }

    public ConfirmPageBase(MenuType menu, String title, int rows) {
        super(menu, title, 6);
    }

    public ItemStack getConfirmOption() {
        return _confirmOption;
    }

    public void setConfirmOption(ItemStack confirmOption) {
        _confirmOption = confirmOption;
    }

    public ConfirmPageCallback getCallback() {
        return _callback;
    }

    public void setCallback(ConfirmPageCallback callback) {
        _callback = callback;
    }

    private void addButtons() {

        addButton(22, _confirmOption);

        addButton(37, new ItemBuilder(Material.WOOL, ChatColor.GREEN + "Confirm").setData(DyeColor.LIME.getWoolData()).build(), new IButtonCallback() {

            @Override
            public void onClick(Page page, InventoryClickEvent event) {

                event.getWhoClicked().closeInventory();

                if (!(event.getWhoClicked() instanceof Player))
                    return;

                _callback.confirm((Player) event.getWhoClicked());

            }

        });

        addButton(43, new ItemBuilder(Material.WOOL, ChatColor.RED + "Cancel").setData(DyeColor.RED.getWoolData()).build(), new IButtonCallback() {

            @Override
            public void onClick(Page page, InventoryClickEvent event) {

                event.getWhoClicked().closeInventory();

                if (!(event.getWhoClicked() instanceof Player))
                    return;

                _callback.cancel((Player) event.getWhoClicked());

            }

        });

    }

}
