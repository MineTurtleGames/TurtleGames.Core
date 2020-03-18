package co.turtlegames.core.infraction;

public enum InfractionType {

    WARN("Warning"), MUTE("Mute"), BAN("Ban");

    private String _name;

    InfractionType(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

}
