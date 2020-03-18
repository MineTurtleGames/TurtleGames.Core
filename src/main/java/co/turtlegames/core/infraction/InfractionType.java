package co.turtlegames.core.infraction;

public enum InfractionType {

    WARN("Warning", 13), MUTE("Mute", 14), SHADOW_MUTE("Shadow Mute", 15), BAN("Ban", 16);

    private String _name;
    private int _uiSlot;

    InfractionType(String name, int uiSlot) {
        _name = name;
        _uiSlot = uiSlot;
    }

    public String getName() {
        return _name;
    }

    public int getUiSlot() {
        return _uiSlot;
    }

}
