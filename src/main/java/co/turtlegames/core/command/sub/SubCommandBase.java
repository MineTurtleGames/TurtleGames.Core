package co.turtlegames.core.command.sub;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;

public abstract class SubCommandBase<B extends TurtleModule> extends CommandBase<B> {

    public SubCommandBase(CommandBase<B> command, Rank requiredRank, String... aliases) {
        super(command.getModule(), requiredRank, aliases);
    }

}
