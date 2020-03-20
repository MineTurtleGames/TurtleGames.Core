package co.turtlegames.core.command;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public abstract class CommandBase<ModuleType extends TurtleModule> extends BukkitCommand {

    private TurtleModule _module;

    private Rank _minRank;
    private HashSet<Rank> _alternateRanks;

    public CommandBase(ModuleType module, Rank requiredRank, String... aliases) {

        super(aliases[0]);

        _module = module;
        _minRank = requiredRank;
        _alternateRanks = new HashSet<>();

        setAliases(Arrays.asList(aliases));

    }

    @Override
    public boolean execute(CommandSender commandSender, String cmd, String[] args) {

        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player) commandSender;

        ProfileManager profileManager = _module.getModule(ProfileManager.class);

        profileManager.fetchProfile(player.getUniqueId()).thenAccept((playerProfile -> {

            boolean canRunCommand = playerProfile.getRank().isPermissible(_minRank);

            if(!canRunCommand)
                canRunCommand = _alternateRanks.contains(playerProfile.getRank());

            if (!canRunCommand) {

                player.sendMessage(Chat.main(profileManager.getName(),
                        "You must be permission level " + Chat.elem(_minRank.getName()) + " or higher to execute this command (You are " + playerProfile.getRank().getName() + ")"));
                return;

            }

            try {
                executeCommand(playerProfile, args);
            } catch(Exception ex) {
                ex.printStackTrace();
                player.sendMessage(Chat.main("Error", "An error occurred while executing the command"));
            }

        }));

        return true;
    }

    public abstract void executeCommand(PlayerProfile profile, String args[]);

    public ModuleType getModule() {
        return (ModuleType) _module;
    }

    public void register() {

        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            commandMap.register("TurtleCommand", this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
