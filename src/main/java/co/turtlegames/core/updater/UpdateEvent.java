package co.turtlegames.core.updater;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private UpdateType _type;

    public UpdateEvent(UpdateType type) {
        _type = type;
    }

    public UpdateType getType() {
        return _type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
