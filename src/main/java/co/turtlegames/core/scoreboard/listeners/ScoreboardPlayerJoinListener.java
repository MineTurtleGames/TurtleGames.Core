package co.turtlegames.core.scoreboard.listeners;

import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardPlayerJoinListener implements Listener {

    private TurtleScoreboardManager _scoreboardManager;

    public ScoreboardPlayerJoinListener(TurtleScoreboardManager scoreboardManager) {
        _scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        _scoreboardManager.pokeAllTeamData(event.getPlayer());

        // load scoreboard
        TurtlePlayerScoreboard playerScoreboard = _scoreboardManager.getScoreboard(event.getPlayer());

    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {

        _scoreboardManager.purgeCache(event.getPlayer());

    }

}
