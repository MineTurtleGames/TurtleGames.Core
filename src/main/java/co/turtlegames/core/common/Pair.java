package co.turtlegames.core.common;

public class Pair<KeyType, ValueType> {

    private KeyType _key;
    private ValueType _value;

    public Pair(KeyType key, ValueType value) {

        _key = key;
        _value = value;

    }

    public KeyType getKey() {
        return _key;
    }

    public ValueType getValue() {
        return _value;
    }

    public void setKey(KeyType key) {
        _key = key;
    }

    public void setValue(ValueType value) {
        _value = value;
    }

}
