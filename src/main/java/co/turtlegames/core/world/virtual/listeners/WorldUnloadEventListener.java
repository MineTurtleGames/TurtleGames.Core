package co.turtlegames.core.world.virtual.listeners;

import co.turtlegames.core.world.virtual.VirtualWorldManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldUnloadEventListener implements Listener {

    private VirtualWorldManager _virtualWorldManager;

    public WorldUnloadEventListener(VirtualWorldManager virtualWorldManager) {
        _virtualWorldManager = virtualWorldManager;
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {

        if(_virtualWorldManager.isVirtualWorld(event.getWorld())) {

            event.setCancelled(true);
            System.out.println("Attempting to unload virtual world through regular Bukkit::unloadWorld procedure!");

        }

    }

}
