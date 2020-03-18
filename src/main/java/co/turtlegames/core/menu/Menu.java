package co.turtlegames.core.menu;

import co.turtlegames.core.TurtleModule;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public abstract class Menu<ModuleType extends TurtleModule> implements Listener {

    private ModuleType _module;
    private WeakReference<Player> _owner;

    private Inventory _inventory;

    private String _title;
    private Stack<Page<? extends Menu>> _pageStack;

    private HashMap<Integer, IButtonCallback> _activeCallbacks;

    public Menu(ModuleType module, String title, Player owner) {

        module.getPlugin().getServer().getPluginManager().registerEvents(this, module.getPlugin());

        _module = module;
        _owner = new WeakReference<Player>(owner);

        _inventory = Bukkit.createInventory(owner, 54, title);

        _title = title;
        _pageStack = new Stack<>();

        _activeCallbacks = new HashMap<>();

    }

    public WeakReference<Player> getOwner() {
        return _owner;
    }

    public ModuleType getModule() {
        return _module;
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

    protected void addPage(Page<? extends Menu> page) {

        _pageStack.add(page);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTopInventory().equals(_inventory))
            event.setCancelled(true);

        if (event.getClickedInventory().equals(_inventory)) {

            if (_activeCallbacks.containsKey(event.getSlot())) {
                _activeCallbacks.get(event.getSlot()).onClick(_pageStack.peek(), event);
            }

        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getView().getTopInventory().equals(_inventory)) {

            HandlerList.unregisterAll(this);

        }

    }

    public Inventory getInventory() {
        return _inventory;
    }
}
