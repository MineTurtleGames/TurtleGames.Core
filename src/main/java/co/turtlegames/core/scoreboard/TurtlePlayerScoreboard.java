package co.turtlegames.core.scoreboard;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.util.UtilScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class TurtlePlayerScoreboard {

    private TurtleScoreboardManager _scoreboardManager;

    private WeakReference<Player> _player;

    private Scoreboard _playerScoreboard;
    private Objective _sidebarObjective;

    private ScoreboardView _activeView;

    private String[] _slots;

    public TurtlePlayerScoreboard(TurtleScoreboardManager scoreboardManager, Player player) {

        _scoreboardManager = scoreboardManager;

        _player = new WeakReference<>(player);
        _slots = new String[16];

    }

    public void initializeScoreboard() {

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Player ply = _player.get();

        if(ply == null)
            return;

        _playerScoreboard = scoreboardManager.getNewScoreboard();

        _sidebarObjective = _playerScoreboard.registerNewObjective("sidebar", "dummy");

        _sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.initializeTeamData();
        ply.setScoreboard(_playerScoreboard);

    }

    public void initializeTeamData() {

        for(Player player : Bukkit.getOnlinePlayers())
            this.promptLoadTeamData(player);

    }

    public void promptLoadTeamData(Player ply) {

        Team cachedTeam = _playerScoreboard.getTeam(ply.getName());

        CompletableFuture<PlayerProfile> future = _scoreboardManager.getModule(ProfileManager.class)
                                                        .fetchProfile(ply.getUniqueId());

        if(cachedTeam == null)
            cachedTeam = _playerScoreboard.registerNewTeam(ply.getName());

        final Team team = cachedTeam;

        team.setPrefix(ChatColor.DARK_GRAY + "[...] ");
        team.addEntry(ply.getName());

        future.thenAccept((PlayerProfile profile) -> {

            if(profile == null || profile == ProfileManager.LOADING)
                return;

            team.setPrefix(profile.getRank().getTag() + ChatColor.GRAY + " ");
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);

        });

    }

    public void clearScoreboard() {

        _slots = new String[16];

        for(int i = 0; i < 16 ; i ++)
            this.setLine(i, null);

    }

    public void setLine(int line, String value) {

        int realScore = 15 - line;

        if(value == null || value.length() == 0)
            value = "" + UtilScoreboard.getUniqueChatColor(line);

        if(_slots[line] != null) {

            if(_slots[line].equals(value))
                return;

            _playerScoreboard.resetScores(_slots[line]);

        }

        Score score = _sidebarObjective.getScore(value);
        score.setScore(realScore);

        _slots[line] = value;

    }

    public void setTitle(String name) {
        _sidebarObjective.setDisplayName(name);
    }

    public void setActiveView(ScoreboardView view) {

        _activeView = view;

        this.clearScoreboard();
        _activeView.initializeBoard(this);

    }

    public void update() {
        _activeView.updateBoard(this);
    }

}
