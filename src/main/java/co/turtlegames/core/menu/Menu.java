package co.turtlegames.core.menu;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public abstract class Menu implements Listener {

    private JavaPlugin _hostPlugin;
    private WeakReference<Player> _owner;

    private Inventory _inventory;

    private String _title;
    private Stack<Page> _pageStack;

    private HashMap<Integer, IButtonCallback> _activeCallbacks;

    public Menu(JavaPlugin hostPlugin, String title, Player owner) {

        _hostPlugin.getServer().getPluginManager().registerEvents(this, _hostPlugin);

        _hostPlugin = hostPlugin;
        _owner = new WeakReference(owner);

        _inventory = Bukkit.createInventory(owner, 54, title);

        _title = title;
        _pageStack = new Stack<>();

        _activeCallbacks = new HashMap<>();

    }

    public void open() {

        if (_owner.get() == null)
            return;

        populateMenu();
        _owner.get().openInventory(_inventory);

    }

    public void back() {

        _pageStack.push(_pageStack.pop());
        populateMenu();
        open();

    }

    public void populateMenu() {

        _activeCallbacks.clear();

        Page currentPage = _pageStack.peek();

        if (_owner.get() == null)
            return;

        _inventory = Bukkit.createInventory(_owner.get(), currentPage.getRows() * 9, StringUtils.defaultIfEmpty(currentPage.getTitle(), _title));

        List<Button> buttons = currentPage.getButtons();

        for (Button button : buttons) {

            _inventory.setItem(button.getSlot(), button.getItem());

            if (button.getCallback() == null)
                continue;

            _activeCallbacks.put(button.getSlot(), button.getCallback());

        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == _inventory) {

            if (_activeCallbacks.containsKey(event.getSlot())) {
                _activeCallbacks.get(event.getSlot()).onClick(_pageStack.peek(), event);
            }

        }

    }

    public Inventory getInventory() {
        return _inventory;
    }
}
