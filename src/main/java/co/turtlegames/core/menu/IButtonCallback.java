package co.turtlegames.core.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface IButtonCallback<PageType extends Page> {

    void onClick(PageType page, InventoryClickEvent event);

}
