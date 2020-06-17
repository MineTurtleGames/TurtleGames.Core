package co.turtlegames.core.inventory;

import co.turtlegames.core.currency.CurrencyType;

import java.util.Map;

public interface IItemPurchasable {

    public Map<CurrencyType, Integer> getCost();
    public void onPurchase();

}
