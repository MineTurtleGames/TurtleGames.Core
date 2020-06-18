package co.turtlegames.core.inventory;

import java.util.Map;

public interface IItemMetaData {

    public Map<String, Object> serialize();
    public void loadFrom(Map<String, String> data);

}
