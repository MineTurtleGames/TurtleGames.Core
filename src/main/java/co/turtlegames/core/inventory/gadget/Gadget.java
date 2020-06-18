package co.turtlegames.core.inventory.gadget;

import co.turtlegames.core.currency.CurrencyType;
import co.turtlegames.core.inventory.AbstractItem;
import co.turtlegames.core.inventory.IItemConsumable;
import co.turtlegames.core.inventory.IItemMetaData;
import co.turtlegames.core.inventory.IItemPurchasable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Gadget extends AbstractItem implements IItemConsumable, IItemPurchasable, IItemMetaData {

    private int _quantity = 0;
    private int _cost;
    private int _purchaseQuantity;

    public Gadget(UUID owner, String type, String displayName, int cost, int purchaseQuantity) {

        super(owner, type, displayName);
        _cost = cost;
        _purchaseQuantity = purchaseQuantity;

    }

    public abstract void onUse(Player player);

    public void onConsume() {

        if (!canConsume())
            return;

        Player player = Bukkit.getPlayer(getOwner());

        if (player != null)
            onUse(player);

        _quantity--;

    }

    public boolean canConsume() {
        return _quantity > 0;
    }

    public Map<CurrencyType, Integer> getCost() {

        HashMap<CurrencyType, Integer> cost = new HashMap<>();
        cost.put(CurrencyType.TOKENS, _cost);

        return cost;

    }

    public void onPurchase() {
        _quantity += _purchaseQuantity;
    }

    public void loadFrom(String data) {
        _quantity = Integer.parseInt(data.split(":")[1]);
    }

    public Map<String, Object> serialize() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("quantity", _quantity);

        return data;

    }

}
