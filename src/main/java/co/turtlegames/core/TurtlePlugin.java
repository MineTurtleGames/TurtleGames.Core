package co.turtlegames.core;

import org.bukkit.plugin.java.JavaPlugin;

public class TurtlePlugin extends JavaPlugin {

    private String _name;

    public TurtlePlugin(String name) {
        _name = name;
    }

    protected TurtleCore getCoreInstance() {
        return TurtleCore.getInstance();
    }

}
