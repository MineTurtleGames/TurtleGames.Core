package co.turtlegames.core.scoreboard;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.util.StringChunker;
import co.turtlegames.core.util.UtilScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

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

        this.clearScoreboard();

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

            CoreNameColourEvent nameColourEvent = new CoreNameColourEvent(profile);
            Bukkit.getPluginManager().callEvent(nameColourEvent);

            team.setPrefix(profile.getRank().getTag() + nameColourEvent.getNameColour() + (profile.getRank() != Rank.PLAYER ? " " : ""));

            team.setNameTagVisibility(NameTagVisibility.ALWAYS);

        });

    }

    public void clearScoreboard() {

        for(int i = 0; i <= 15; i++)
            this.setLine(i, null);

    }

    public void setLine(int line, String value) {

        int lineScore = 16 - line;
        String entryName = UtilScoreboard.getUniqueChatColor(line) + ChatColor.RESET.toString();

        Team team = _playerScoreboard.getEntryTeam(entryName);

        if(value == null) {

            if(team == null)
                return;

            _playerScoreboard.resetScores(entryName);
            team.unregister();

            return;

        }

        if(team == null) {

            team = _playerScoreboard.registerNewTeam(entryName);
            team.addEntry(entryName);

            _sidebarObjective.getScore(entryName).setScore(lineScore);

        }

        String[] lineSplit = StringChunker.chunk(value, 16);

        if(lineSplit.length > 0)
            team.setPrefix(lineSplit[0]);

        if(lineSplit.length > 1 && lineSplit[1] != null)
            team.setSuffix(lineSplit[1]);

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

    public Player getOwner() {
        return _player.get();
    }

    public ScoreboardView getView() {
        return _activeView;
    }

}
