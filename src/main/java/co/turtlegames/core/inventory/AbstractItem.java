package co.turtlegames.core.inventory;

import java.util.UUID;

public abstract class AbstractItem {

    private int _id;
    private UUID _owner;
    private String _type;
    private String _displayName;

    public AbstractItem(UUID owner, String type, String displayName) {

        _owner = owner;
        _type = type;
        _displayName = displayName;

    }

    public int getId() {
        return _id;
    }

    public UUID getOwner() {
        return _owner;
    }

    public void setOwner(UUID owner) {
        _owner = owner;
    }

    public String getType() {
        return _type;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setId(int id) {
        _id = id;
    }

}
