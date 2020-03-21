package co.turtlegames.core.chat.dm;

import co.turtlegames.core.TurtleModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectMessageManager extends TurtleModule {

    private Map<UUID, UUID> _dmResponseMap;

    public DirectMessageManager(JavaPlugin plugin) {

        super(plugin, "Direct Message Manager");
        _dmResponseMap = new HashMap<>();

    }

    @Override
    public void initializeModule() {
        this.registerCommand(new CommandDirectMessage(this));
    }

    public void pushLastMessageSender(UUID to, UUID from) {
        _dmResponseMap.put(to, from);
    }

    public UUID getLastMessageSender(UUID to) {
        return _dmResponseMap.get(to);
    }

}
