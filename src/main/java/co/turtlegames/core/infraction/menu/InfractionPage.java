package co.turtlegames.core.infraction.menu;

import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.infraction.InfractionType;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class InfractionPage extends Page<InfractionMenu> {

    private InfractionType _selectedType;
    private int _selectedSeverity;
    private int _currentSeveritySlot;

    public InfractionPage(InfractionMenu menu) {

        super(menu, 6);

        _selectedType = InfractionType.WARN;
        _selectedSeverity = 1;
        _currentSeveritySlot = 31;

        // Player head
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(menu.getTarget().getName());
        meta.setDisplayName(ChatColor.YELLOW + menu.getTarget().getName());
        meta.setLore(Arrays.asList(ChatColor.WHITE + menu.getReason()));
        head.setItemMeta(meta);

        addButton(10, head);

        addButton(13, new ItemBuilder(Material.PAPER, ChatColor.DARK_GREEN + "Warning").hideEnchants().glow().build(), (page, event) -> selectType(InfractionType.WARN));

        addButton(14, new ItemBuilder(Material.BOOK, ChatColor.DARK_GREEN + "Mute").hideEnchants().build(), (page, event) -> selectType(InfractionType.MUTE));

        addButton(15, new ItemBuilder(Material.BOOK_AND_QUILL, ChatColor.DARK_GREEN + "Shadow Mute").hideEnchants().build(), (page, event) -> selectType(InfractionType.SHADOW_MUTE));

        addButton(16, new ItemBuilder(Material.IRON_SWORD, ChatColor.DARK_GREEN + "Ban").hideEnchants().build(), (page, event) -> selectType(InfractionType.BAN));

        addButton(31, new ItemBuilder(Material.INK_SACK, ChatColor.GREEN + "Severity 1").hideEnchants().glow().setData(DyeColor.GREEN.getDyeData()).build(), (page, event) -> selectSeverity(1));

        addButton(32, new ItemBuilder(Material.INK_SACK, ChatColor.YELLOW + "Severity 2").hideEnchants().setData(DyeColor.YELLOW.getDyeData()).build(), (page, event) -> selectSeverity(2));

        addButton(33, new ItemBuilder(Material.INK_SACK, ChatColor.RED + "Severity 3").hideEnchants().setData(DyeColor.RED.getDyeData()).build(), (page, event) -> selectSeverity(3));

        addButton(34, new ItemBuilder(Material.EMERALD_BLOCK, ChatColor.DARK_RED + "Permanent").hideEnchants().build(), (page, event) -> selectSeverity(4));

        addButton(28, new ItemBuilder(Material.INK_SACK, ChatColor.LIGHT_PURPLE + "Apply Infraction").setData(DyeColor.CYAN.getDyeData()).build(), (page, event) -> {

            long duration = -1;

            if (_selectedType == InfractionType.MUTE || _selectedType == InfractionType.SHADOW_MUTE) {

                if (_selectedSeverity == 1) {
                    duration = 1000L * 60 * 60 * 2;
                } else if (_selectedSeverity == 2) {
                    duration = 1000L * 60 * 60 * 24;
                } else if (_selectedSeverity == 3) {
                    duration = 1000L * 60 * 60 * 24 * 7;
                }

            }

            if (_selectedType == InfractionType.BAN) {

                if (_selectedSeverity == 1) {
                    duration = 1000L * 60 * 60 * 24;
                } else if (_selectedSeverity == 2) {
                    duration = 1000L * 60 * 60 * 24 * 7;
                } else if (_selectedSeverity == 3) {
                    duration = 1000L * 60 * 60 * 24 * 30;
                }

            }

            Infraction infraction = new Infraction(getMenu().getTarget().getUniqueId(), getMenu().getOwner().get().getUniqueId(), _selectedType, System.currentTimeMillis(), duration, getMenu().getReason());

            getMenu().getModule().registerInfraction(infraction);

        });

    }

    private void selectSeverity(int severity) {

        int uiSlot = 31;

        if (severity == 2)
            uiSlot = 32;
        else if (severity == 3)
            uiSlot = 33;
        else if (severity == 4)
            uiSlot = 34;

        ItemStack item = getMenu().getInventory().getItem(_currentSeveritySlot);
        ItemMeta meta = item.getItemMeta();
        meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
        item.setItemMeta(meta);

        item = getMenu().getInventory().getItem(uiSlot);
        meta = item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        item.setItemMeta(meta);

        _currentSeveritySlot = uiSlot;
        _selectedSeverity = severity;

    }

    private void selectType(InfractionType type) {

        ItemStack item = getMenu().getInventory().getItem(_selectedType.getUiSlot());
        ItemMeta meta =item.getItemMeta();
        meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
        item.setItemMeta(meta);

        item = getMenu().getInventory().getItem(type.getUiSlot());
        meta = item.getItemMeta();
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        item.setItemMeta(meta);

        _selectedType = type;


    }

}
