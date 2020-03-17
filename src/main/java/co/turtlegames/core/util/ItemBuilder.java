package co.turtlegames.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private ItemStack _item;

    public ItemBuilder(Material material) {

        _item = new ItemStack(material);

    }

    public ItemBuilder(Material material, String name) {

        this(material);

        ItemMeta meta = _item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + name);
        _item.setItemMeta(meta);

    }

    public ItemBuilder setAmount(int amount) {

        _item.setAmount(amount);
        return this;

    }

    public ItemBuilder setData(byte data) {

        _item.getData().setData(data);
        return this;

    }

    public ItemBuilder setLore(String... lore) {

        ItemMeta meta = _item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        _item.setItemMeta(meta);

        return this;

    }

    public ItemStack build() {
        return _item;
    }

}
