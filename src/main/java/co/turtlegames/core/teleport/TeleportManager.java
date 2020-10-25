package co.turtlegames.core.teleport;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.teleport.command.TeleportCommand;

public class TeleportManager extends TurtleModule {

    public TeleportManager(TurtleCore core) {
        super(core, "Teleport");
    }

    @Override
    public void initializeModule() {
        registerCommand(new TeleportCommand(this));
    }

}
