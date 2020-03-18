package co.turtlegames.core.scoreboard;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.scoreboard.listeners.ScoreboardPlayerJoinListener;
import co.turtlegames.core.scoreboard.views.DefaultScoreboardView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TurtleScoreboardManager extends TurtleModule {

    private ScoreboardView _activeView;
    private HashMap<UUID, TurtlePlayerScoreboard> _sbMap;

    public TurtleScoreboardManager(TurtleCore mainInstance) {

        super(mainInstance, "Scoreboard Manager");

        this.registerListener(new ScoreboardPlayerJoinListener(this));

        _sbMap = new HashMap<>();

    }

    @Override
    public void initializeModule() {

        _activeView = new DefaultScoreboardView();

        for(Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(Chat.main("Scoreboard", "Your scoreboard instance was reset"));
            this.getScoreboard(player);

        }

    }

    public TurtlePlayerScoreboard getScoreboard(Player ply) {

        if(_sbMap.containsKey(ply.getUniqueId()))
            return _sbMap.get(ply.getUniqueId());

        TurtlePlayerScoreboard scoreboard = new TurtlePlayerScoreboard(this, ply);
        scoreboard.initializeScoreboard();

        scoreboard.setActiveView(_activeView);

        _sbMap.put(ply.getUniqueId(), scoreboard);
        return scoreboard;

    }

    public void updateScoreboardView(ScoreboardView view) {

        _activeView = view;

        for(TurtlePlayerScoreboard scoreboard : _sbMap.values())
            scoreboard.setActiveView(_activeView);

    }

    public void updateAll() {

        for(TurtlePlayerScoreboard scoreboard : _sbMap.values())
            scoreboard.update();

    }

    public void purgeCache(Player player) {
        _sbMap.remove(player.getUniqueId());
    }

}
