package co.turtlegames.core.profile;

import org.bukkit.ChatColor;

public enum Rank {

    PLAYER("Player", ChatColor.WHITE, ""),
    PRO("Pro", ChatColor.YELLOW),

    BUILDER("Builder", ChatColor.BLUE),

    JNR_MODERATOR("Jr.Mod", ChatColor.GREEN),
    MODERATOR("Mod", ChatColor.DARK_GREEN),
    SNR_MODERATOR("Sr.Mod", ChatColor.DARK_GREEN),

    ADMINISTRATOR("Admin", ChatColor.RED),
    OWNER("Owner", ChatColor.DARK_RED);

    private String _name;
    private ChatColor _color;
    private String _tag;

    Rank(String name, ChatColor color) {

        _name = name;
        _color = color;
        _tag = null;

    }

    Rank(String name, ChatColor color, String tag) {

        _name = name;
        _color = color;
        _tag = tag;

    }

    public String getName() {
        return _name;
    }

    public ChatColor getColor() {
        return _color;
    }

    public String getTag() {

        if (_tag == null)
            return _color + "[" + _name + "]";

        return _tag;

    }

    public boolean isPermissible(Rank comparedTo) {
        return this.ordinal() >= comparedTo.ordinal();
    }

}
