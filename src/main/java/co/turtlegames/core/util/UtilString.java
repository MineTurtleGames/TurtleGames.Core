package co.turtlegames.core.util;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;

public class UtilString {

    public static String formatBoolean(boolean value) {

        if (value)
            return ChatColor.GREEN + "True";

        return ChatColor.RED + "False";

    }

    public static String formatTime(long timeInMs) {

        double seconds = timeInMs/1000.0d;
        double minute = seconds/60.0d;
        double hour = minute/60.0d;
        double day = hour/24.0d;

        DecimalFormat df = new DecimalFormat("#.#");
        if(timeInMs < 0)
        {
            return "Forever";
        }
        if(seconds < 60)
        {
            return df.format(seconds) + " seconds";
        }
        if(minute < 60)
        {
            return df.format(minute) + " minutes";
        }
        if(hour < 24)
        {
            return df.format(hour) + " hours";
        }
        return df.format(day) + " days";

    }

}
