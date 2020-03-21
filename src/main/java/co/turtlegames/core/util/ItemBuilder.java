package co.turtlegames.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        _item.setDurability(data);
        return this;

    }

    public ItemBuilder setLore(String... lore) {

        ItemMeta meta = _item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        _item.setItemMeta(meta);

        return this;

    }

    public ItemBuilder addLore(Collection<String> lore) {

        ItemMeta meta = _item.getItemMeta();
        List<String> currentLore = meta.getLore();
        currentLore.addAll(lore);
        meta.setLore(currentLore);
        _item.setItemMeta(meta);

        return this;

    }

    public ItemBuilder hideEnchants() {

        ItemMeta meta = _item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        _item.setItemMeta(meta);

        return this;

    }

    public ItemBuilder glow() {

        ItemMeta meta = _item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        _item.setItemMeta(meta);

        return this;

    }

    public ItemStack build() {
        return _item;
    }

}
