package co.turtlegames.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilDev {

    public enum AlertLevel {

        LOG,
        WARN,
        FATAL;

    }

    public static void alert(AlertLevel level, String message) {

        for(Player ply: Bukkit.getOnlinePlayers()) {

            if(ply.isOp())
                ply.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "LOG " + ChatColor.RESET + message);

        }

        System.err.println(message);
    }

}
