package co.turtlegames.core.scoreboard;

import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CoreNameColourEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private PlayerProfile _playerProfile;
    private ChatColor _nameColour = ChatColor.GRAY;

    public CoreNameColourEvent(PlayerProfile playerProfile) {
        _playerProfile = playerProfile;
    }

    public ChatColor getNameColour() {
        return _nameColour;
    }

    public void setNameColour(ChatColor colour) {
        _nameColour = colour;
    }

    public PlayerProfile getPlayerProfile() {
        return _playerProfile;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
