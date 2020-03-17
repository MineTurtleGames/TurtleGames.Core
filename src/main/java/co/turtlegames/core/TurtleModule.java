package co.turtlegames.core;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.db.DatabaseConnector;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class TurtleModule implements Listener {

    private TurtleCore _coreInstance;

    private JavaPlugin _plugin;
    private String _name;

    public TurtleModule(JavaPlugin plugin, String name) {

        _plugin = plugin;
        _name = name;

        this.assignCoreInstance();
    }

    private void assignCoreInstance() {

        Plugin javaPlugin = Bukkit.getPluginManager().getPlugin("tCore");

        if(!(javaPlugin instanceof TurtleCore)) {

            System.err.println("Bruh");
            return;

        }

        _coreInstance = (TurtleCore) javaPlugin;

    }

    public JavaPlugin getPlugin() {
        return _plugin;
    }

    public String getName() {
        return _name;
    }

    public <I extends TurtleModule> I getModule(Class<I> clazz) {
        return _coreInstance.getModule(clazz);
    }

    public DatabaseConnector getDatabaseConnector() {
        return _coreInstance.getDatabaseConnector();
    }

    protected void registerListener(Listener listener) {
        _plugin.getServer().getPluginManager().registerEvents(listener, _plugin);
    }

    protected void registerCommand(CommandBase commandBase) {
        commandBase.register();
    }

    public abstract void initializeModule();

}
