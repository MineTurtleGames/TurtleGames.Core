package co.turtlegames.core.db;

public class DatabaseException extends Exception {

    public enum FailureType {

        NOT_FOUND,
        MISC;

    }

    private FailureType _type;

    public DatabaseException(FailureType type, String message) {

        super(message);

        _type = type;

    }

    public FailureType getFailureType() {
        return _type;
    }

}
