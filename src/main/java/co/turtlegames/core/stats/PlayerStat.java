package co.turtlegames.core.stats;

public class PlayerStat {

    private PlayerStatData _ownerData = null;

    // TODO: Replace with integer ids to save space
    private String _gameInternalId = "global";
    private String _statInternalId;

    private double _value;

    public PlayerStat(String gameId, String statId, double value) {

        _gameInternalId = gameId;
        _statInternalId = statId;

        _value = value;

    }

    public String getCombinedId() {
        return _gameInternalId + "." + _statInternalId;
    }

    public double getValue() {
        return _value;
    }

    protected void annex(PlayerStatData data) {

        if(_ownerData != null)
            return;

        _ownerData = data;

    }

    public String getGame() {
        return _gameInternalId;
    }

    public String getName() {
        return _statInternalId;
    }

    void updateValue(double newValue) {
        _value = newValue;
    }

    public void increment(int amount) {
        _ownerData.incrementStat(this, amount);
    }
}
