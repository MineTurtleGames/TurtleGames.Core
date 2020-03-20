package co.turtlegames.core.common;

import co.turtlegames.core.chat.CoreChatEvent;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.util.UtilString;
import co.turtlegames.core.util.UtilXp;
import org.bukkit.ChatColor;

public class Chat {

    private static final String CHAT_HEADER_PREFIX = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString();
    private static final String CHAT_HEADER_SUFFIX = "\u226B " + ChatColor.RESET;

    private static final String CHAT_BODY_COLOR = ChatColor.GREEN.toString();
    private static final String CHAT_ELEMENT_COLOR = ChatColor.GOLD.toString();

    public static String main(String headerName, String body) { return header(headerName) + body(body); }

    public static String header(String headerName) {
        return CHAT_HEADER_PREFIX + ">> ";
    }

    public static String body(String content) {
        return CHAT_BODY_COLOR + content;
    }

    public static String elem(String element) {
        return CHAT_ELEMENT_COLOR + element + CHAT_BODY_COLOR;
    }

    public static String chatMessage(CoreChatEvent event, String message) {

        PlayerProfile profile = event.getSender();

        return UtilXp.getLevelTag(UtilXp.getLevel(profile.getXp()))
                + " " + profile.getRank().getTag() + (profile.getRank() == Rank.PLAYER ? "" : " ")
                + event.getNameColor() + profile.getOwner().getDisplayName()
                + ChatColor.GRAY + ": " + ChatColor.WHITE + message;

    }

    public static String getBanMessage(Infraction infraction) {

        return ChatColor.RESET.toString() + ChatColor.GREEN + ChatColor.BOLD.toString() + "TURTLE GAMES - ACCOUNT SUSPENSION\n"
                + ChatColor.RESET.toString() + ChatColor.RED + "Your ability to connect to our network has been suspended"
                + ChatColor.GRAY + "\n\nThe suspension will expire in " + Chat.elem(UtilString.formatTime(infraction.getMsUntilExpiry())
                + "\n\n" + ChatColor.GRAY + "Reason: " + Chat.elem(infraction.getReason())
                + "\n" + ChatColor.GRAY + "Additional information and appeals can be found at " + Chat.elem("trtlgam.es/ban"));

    }
}
