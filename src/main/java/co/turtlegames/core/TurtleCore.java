package co.turtlegames.core;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.chat.dm.DirectMessageManager;
import co.turtlegames.core.currency.CurrencyManager;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.file.FileClusterManager;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.inventory.InventoryManager;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.recharge.RechargeManager;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import co.turtlegames.core.stats.PlayerStatManager;
import co.turtlegames.core.tab.TabManager;
import co.turtlegames.core.teleport.TeleportManager;
import co.turtlegames.core.updater.UpdateEventManager;
import co.turtlegames.core.util.AuthInfo;
import co.turtlegames.core.world.virtual.VirtualWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TurtleCore extends JavaPlugin {

    private static TurtleCore _coreSingleton = null;

    protected static TurtleCore getInstance() {
        return _coreSingleton;
    }

    private DatabaseConnector _connector;
    private Map<Class<? extends TurtleModule>, TurtleModule> _registeredModules;

    @Override
    public void onEnable() {

        _coreSingleton = this;

        Properties dbProperties = new Properties();

        dbProperties.setProperty("serverName", "127.0.0.1");
        dbProperties.setProperty("portNumber", "3306");
        dbProperties.setProperty("databaseName", AuthInfo.MARIA_DATABASE);
        dbProperties.setProperty("user", AuthInfo.MARIA_USER);
        dbProperties.setProperty("password", AuthInfo.MARIA_PASSWORD);

        _connector = new DatabaseConnector(this, dbProperties);
        _registeredModules = new HashMap<>();

        this.registerModule(new ProfileManager(this));
        this.registerModule(new UpdateEventManager(this));
        this.registerModule(new ChatManager(this));
        this.registerModule(new InfractionManager(this));
        this.registerModule(new InventoryManager(this));
        this.registerModule(new RechargeManager(this));
        this.registerModule(new TurtleScoreboardManager(this));
        this.registerModule(new AchievementManager(this));
        this.registerModule(new DirectMessageManager(this));
        this.registerModule(new CurrencyManager(this));
        this.registerModule(new PlayerStatManager(this));
        // this.registerModule(new MetricManager(this));
        this.registerModule(new FileClusterManager(this));
        this.registerModule(new TabManager(this));
        this.registerModule(new TeleportManager(this));

        this.registerModule(new VirtualWorldManager(this));

        Bukkit.getScheduler().runTask(this, () -> {

            this.initializeModules();

        });

    }

    @Override
    public void onDisable() {

        _connector.deinitialize();

        for(TurtleModule module : _registeredModules.values())
            module.deinitializeModule();

    }


    public void registerModule(TurtleModule module) {
        _registeredModules.put(module.getClass(), module);
    }

    public <I> I getModule(Class<? extends TurtleModule> clazz) {
        return (I) _registeredModules.get(clazz);
    }

    public DatabaseConnector getDatabaseConnector() {
        return _connector;
    }

    public void initializeModules() {

        for(TurtleModule module : _registeredModules.values()) {

            try {
                module.initializeModule();
            } catch(Exception ex) {

                System.err.println("An error occurred while starting module " + module.getName() + ": ");
                ex.printStackTrace();

            }
        }

    }

}
