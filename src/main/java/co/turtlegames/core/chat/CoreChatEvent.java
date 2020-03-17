package co.turtlegames.core.chat;

import co.turtlegames.core.profile.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoreChatEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private PlayerProfile _sender;
    private String _message;

    private ChatColor _nameColor = ChatColor.YELLOW;

    public CoreChatEvent(PlayerProfile sender, String message) {

        _sender = sender;
        _message = message;

    }

    public PlayerProfile getSender() {
        return _sender;
    }

    public String getMessage() {
        return _message;
    }

    public ChatColor getNameColor() {
        return _nameColor;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public void setNameColor(ChatColor nameColor) {
        _nameColor = nameColor;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
