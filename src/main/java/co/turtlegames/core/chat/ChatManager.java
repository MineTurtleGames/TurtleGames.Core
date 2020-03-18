package co.turtlegames.core.chat;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.chat.command.SilenceCommand;
import co.turtlegames.core.chat.listeners.ChatFilterListener;
import co.turtlegames.core.chat.listeners.ChatHandleListener;
import co.turtlegames.core.chat.listeners.ChatMiscListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatManager extends TurtleModule {

    private boolean _silenced = false;
    private double _chatSlow = 0D;

    public ChatManager(JavaPlugin plugin) {
        super(plugin, "Chat Manager");
    }

    @Override
    public void initializeModule() {

        this.registerListener(new ChatHandleListener(this));
        this.registerListener(new ChatFilterListener(this));
        this.registerListener(new ChatMiscListener(this));

        this.registerCommand(new SilenceCommand(this));

    }

    public boolean isSilenced() {
        return _silenced;
    }

    public double getChatSlow() {
        return _chatSlow;
    }

    public void setSilenced(boolean silenced) {
        _silenced = silenced;
    }
}
