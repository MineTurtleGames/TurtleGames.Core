package co.turtlegames.core.util;

import org.bukkit.ChatColor;

public class UtilScoreboard {

    private static ChatColor[] UNIQUE_COLORS = new ChatColor[] {

            ChatColor.DARK_GREEN,
            ChatColor.RED,
            ChatColor.DARK_GRAY,
            ChatColor.AQUA,
            ChatColor.BLACK,
            ChatColor.BLUE,
            ChatColor.BOLD,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE,
            ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED,
            ChatColor.GOLD,
            ChatColor.GRAY,
            ChatColor.GREEN,
            ChatColor.ITALIC,
            ChatColor.LIGHT_PURPLE

    };

    public static ChatColor getUniqueChatColor(int id) {
        return UNIQUE_COLORS[id];
    }

}
