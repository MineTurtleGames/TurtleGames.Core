package co.turtlegames.core.achievement;

import org.bukkit.Material;

public enum AchievementCategory {

    GLOBAL("Global", Material.BEACON, new String[] { "Allan please add detail" }),
    ACCOUNT("Account", Material.REDSTONE_COMPARATOR, new String[] { "insert description"}),

    MASTERY("Mastery Challenges", Material.NETHER_STAR, new String[] { "Difficult challenges that", "offer unique rewards on completion" });

    private String _name;
    private Material _icon;

    private String[] _description;

    AchievementCategory(String name, Material icon, String[] description) {
        _name = name;
        _icon = icon;
        _description = description;
    }

    public String getName() {
        return _name;
    }

    public Material getIcon() {
        return _icon;
    }

    public String[] getDescription() {
        return _description;
    }

}
