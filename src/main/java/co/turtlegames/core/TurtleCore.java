package co.turtlegames.core;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.chat.ChatManager;
import co.turtlegames.core.chat.dm.DirectMessageManager;
import co.turtlegames.core.currency.CurrencyManager;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.metrics.Metric;
import co.turtlegames.core.metrics.MetricManager;
import co.turtlegames.core.metrics.type.ServerStatusMetric;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.recharge.RechargeManager;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import co.turtlegames.core.stats.PlayerStatManager;
import co.turtlegames.core.tab.TabManager;
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
        dbProperties.setProperty("databaseName", "turtle");
        dbProperties.setProperty("user", "minecraft");
        dbProperties.setProperty("password", "gary");

        _connector = new DatabaseConnector(this, dbProperties);
        _registeredModules = new HashMap<>();

        this.registerModule(new ProfileManager(this));
        this.registerModule(new ChatManager(this));
        this.registerModule(new InfractionManager(this));
        this.registerModule(new RechargeManager(this));
        this.registerModule(new TurtleScoreboardManager(this));
        this.registerModule(new AchievementManager(this));
        this.registerModule(new DirectMessageManager(this));
        this.registerModule(new CurrencyManager(this));
        this.registerModule(new PlayerStatManager(this));
        this.registerModule(new MetricManager(this)); //46132

        this.registerModule(new TabManager(this));

        Bukkit.getScheduler().runTask(this, this::initializeModules);

        this.<MetricManager>getModule(MetricManager.class).register(new ServerStatusMetric(1));

    }

    @Override
    public void onDisable() {

        this.<MetricManager>getModule(MetricManager.class).register(new ServerStatusMetric(-1));

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

        for(TurtleModule module : _registeredModules.values())
            module.initializeModule();

    }

}
