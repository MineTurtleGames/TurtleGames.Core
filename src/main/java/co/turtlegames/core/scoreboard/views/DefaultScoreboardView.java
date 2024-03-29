package co.turtlegames.core.scoreboard.views;

import co.turtlegames.core.scoreboard.ScoreboardView;
import co.turtlegames.core.scoreboard.TurtlePlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultScoreboardView extends ScoreboardView {

    public DefaultScoreboardView() {

        super("Default");

    }

    @Override
    public void initializeBoard(TurtlePlayerScoreboard scoreboard) {

        scoreboard.setTitle(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "TurtleGames");

        scoreboard.setLine(0, ChatColor.RED + "Jerry");
        scoreboard.setLine(14, ChatColor.GREEN + "www.turtlegames.co");

    }

    @Override
    public void updateBoard(TurtlePlayerScoreboard scoreboard) {

        scoreboard.setLine(1, "" + ThreadLocalRandom.current().nextInt(1000));

    }
}
