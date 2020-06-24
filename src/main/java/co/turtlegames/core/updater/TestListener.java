package co.turtlegames.core.updater;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    public void onUpdate(UpdateEvent event) {

        if (Bukkit.getOfflinePlayer("Alexbruvv").isOnline()) {
            Bukkit.getPlayer("Alexbruvv").sendMessage(event.getType().toString());
        }

    }

}
