package co.turtlegames.core.inventory;

public interface IItemMetaData {

    public String serialize();
    public void loadFrom(String data);

}
