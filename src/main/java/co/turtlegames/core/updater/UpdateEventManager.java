package co.turtlegames.core.updater;

import co.turtlegames.core.TurtleModule;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateEventManager extends TurtleModule {

    public UpdateEventManager(JavaPlugin plugin) {
        super(plugin, "Tick");
    }

    @Override
    public void initializeModule() {

        registerListener(new TestListener());

        getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable() {

            private int i = 0;

            @Override
            public void run() {

                callUpdateEvent(UpdateType.TICK);

                if (i % 20 == 0)
                    callUpdateEvent(UpdateType.SECOND);

                if (i % 1200 == 0)
                    callUpdateEvent(UpdateType.MINUTE);

            }

        }, 0L, 1L);

    }

    private void callUpdateEvent(UpdateType type) {

        UpdateEvent event = new UpdateEvent(type);
        getPlugin().getServer().getPluginManager().callEvent(event);

    }

}
