package co.turtlegames.core.command;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.command.sub.SubCommandBase;
import co.turtlegames.core.common.Chat;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public abstract class CommandBase<ModuleType extends TurtleModule> extends BukkitCommand {

    private TurtleModule _module;

    private Rank _minRank;
    private HashSet<Rank> _alternateRanks;
    private Map<String, SubCommandBase<?>> _subCommands;

    public CommandBase(ModuleType module, Rank requiredRank, String... aliases) {

        super(aliases[0]);

        _module = module;
        _minRank = requiredRank;
        _alternateRanks = new HashSet<>();
        _subCommands = new HashMap<>();

        setAliases(Arrays.asList(aliases));

    }

    public void addSubCommand(SubCommandBase<?> subCommand) {

        for(String alias : subCommand.getAliases())
            _subCommands.put(alias.toLowerCase(), subCommand);

    }

    public void showHelp(Player player) {

    }

    @Override
    public boolean execute(CommandSender commandSender, String cmd, String[] args) {

        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player) commandSender;

        if (args.length > 0) {

            SubCommandBase<?> subCommand = _subCommands.get(args[0].toLowerCase());

            if (subCommand != null) {

                subCommand.execute(commandSender, cmd, Arrays.copyOfRange(args, 1, args.length));
                return true;

            }

        } else if (_subCommands.size() > 0) {
            showHelp(player);

            return true;
        }

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

            for (String alias : this.getAliases()) {

                Command vanillaCommand = commandMap.getCommand(alias);

                if (vanillaCommand != null) {
                    vanillaCommand.unregister(commandMap);
                }

            }

            commandMap.register("TurtleCommand", this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
