package co.turtlegames.core.util;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

public class UtilXp {

    private static double A_CONST = 600D;
    private static double B_CONST = 5000D;

    private static double FACTOR = (B_CONST * B_CONST)/(A_CONST * A_CONST);

    private static long MAX_XP; static {

        MAX_XP = UtilXp.getXpRequired(50);
        System.out.println("Max XP calculated as " + MAX_XP);

    }

    public static long getXpRequired(int level) {
        return (long) (level * ((A_CONST/2.0D) * level + B_CONST));
    }

    public static int getLevel(long xp) {
        return (int) Math.min(50, Math.floor(Math.sqrt(((2d * xp)/A_CONST) + FACTOR) - (B_CONST/A_CONST)));
    }

    public static String getLevelTag(int level) {

        ChatColor color = ChatColor.DARK_GRAY;

        if(level > 50)
            return ChatColor.DARK_GRAY + "[" + ChatColor.RED + level + ChatColor.DARK_GRAY + "]";

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

    public static String drawXpBar(long xp, int length) {

        if(xp >= MAX_XP)
            return ChatColor.GRAY + "[" + ChatColor.YELLOW + Strings.repeat("|", length) + ChatColor.GRAY + "]";

        int currentLevel = UtilXp.getLevel(xp);

        long nextLevelRequiredXp = UtilXp.getXpRequired(currentLevel + 1);
        long previousLevelRequiredXp = UtilXp.getXpRequired(currentLevel);

        long progress = xp - previousLevelRequiredXp;
        long totalDifference = nextLevelRequiredXp - previousLevelRequiredXp;

        double ratio = (1.0d * progress)/totalDifference;

        int filledAmount = (int) Math.floor(length * ratio);
        int unfilledAmount = length - filledAmount;

        String innerBar = ChatColor.AQUA + Strings.repeat("|", filledAmount)
                + ChatColor.WHITE + Strings.repeat("|", unfilledAmount);
        return ChatColor.GRAY + "[" + innerBar + ChatColor.GRAY + "] " + Math.floor(ratio * 100) + "%";

    }

}
