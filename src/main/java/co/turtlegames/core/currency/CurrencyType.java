package co.turtlegames.core.currency;

public enum CurrencyType {

    COINS("Coin", "Coins", 200);

    private String _name;
    private String _singularName;
    private int _defaultBalance;

    CurrencyType(String name, String singularName, int defaultBalance) {
        _name = name;
        _singularName = singularName;
        _defaultBalance = defaultBalance;
    }

    public String format(int amount) {
        return amount + " " + (amount == 1 ? _name : _singularName);
    }

    public String getName() {
        return _name;
    }

    public String getSingularName() {
        return _singularName;
    }

    public int getDefaultBalance() {
        return _defaultBalance;
    }
}
