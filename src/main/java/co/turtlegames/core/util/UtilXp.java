package co.turtlegames.core.util;

import org.bukkit.ChatColor;

public class UtilXp {

    private static double A_CONST = 600D;
    private static double B_CONST = 5000D;

    private static double FACTOR = (B_CONST * B_CONST)/(A_CONST * A_CONST);

    public static long getXpRequired(int level) {
        return (long) (level * ((A_CONST/2L) * level + B_CONST));
    }

    public static int getLevel(long xp) {
        return (int) Math.floor(Math.sqrt(((2d * xp)/A_CONST) + FACTOR) - (B_CONST/A_CONST));
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
