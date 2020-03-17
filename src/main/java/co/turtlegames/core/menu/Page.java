package co.turtlegames.core.menu;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Page<MenuType extends Menu> {

    private MenuType _menu;

    private String _title;
    private int _rows;

    private ArrayList<Button> _buttons;

    public Page(MenuType menu, int rows) {

        _menu = menu;
        _rows = rows;

        _buttons = new ArrayList<>();

    }

    public Page(MenuType menu, String title, int rows) {

        this(menu, rows);

        _title = title;

    }

    protected void addButton(int slot, ItemStack item, IButtonCallback callback) {
        _buttons.add(new Button(slot, item, callback));
    }

    protected void addButton(int slot, ItemStack item) {
        addButton(slot, item, null);
    }

    public MenuType getMenu() {
        return _menu;
    }

    public String getTitle() {
        return _title;
    }

    public int getRows() {
        return _rows;
    }

    public List<Button> getButtons() {
        return _buttons;
    }
}
