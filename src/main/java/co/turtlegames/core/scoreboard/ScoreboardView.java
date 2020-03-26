package co.turtlegames.core.scoreboard;

public abstract class ScoreboardView {

    private String _name;

    public ScoreboardView(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public abstract void initializeBoard(TurtlePlayerScoreboard scoreboard);
    public abstract void updateBoard(TurtlePlayerScoreboard scoreboard);

}
