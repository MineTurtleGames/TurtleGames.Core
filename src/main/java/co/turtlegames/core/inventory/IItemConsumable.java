package co.turtlegames.core.inventory;

import org.bukkit.entity.Player;

public interface IItemConsumable {

    public void onConsume();
    public boolean canConsume();

}
