package co.turtlegames.core.util;

import org.bukkit.ChatColor;

public class UtilXp {

    private static double XP_PER_LEVEL = 100;
    private static double LOW_XP_PER_LEVEL = 1000;

    public static int getLevel(long xp) {
        return 10;
    }

    public static String getLevelTag(int level) {

        ChatColor color = ChatColor.DARK_GRAY;

        if(level < 5) {
            color = ChatColor.DARK_GRAY;
        } else if(level < 10) {
            color = ChatColor.GRAY;
        } else if (level < 20) {
            color = ChatColor.BLUE;
        } else if(level < 30) {
            color = ChatColor.DARK_GREEN;
        } else if(level < 40) {
            color = ChatColor.GOLD;
        } else if(level < 50) {
            color = ChatColor.RED;
        } else if(level == 50) {
            color = ChatColor.DARK_RED;
        }

        return color + "[" + level + "]";

    }

}
