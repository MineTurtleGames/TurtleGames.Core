package co.turtlegames.core.tab;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;

public class TabManager extends TurtleModule implements Listener {

    private static String TAB_HEADER = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Turtle" + ChatColor.GREEN + ChatColor.BOLD.toString() + "Games";
    private static String TAB_FOOTER = "Jeff";

    public TabManager(TurtleCore core) {

        super(core, "Tab Manager");

        this.registerListener(this);

    }

    @Override
    public void initializeModule() { }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {

        Player ply = ev.getPlayer();

        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + TAB_HEADER + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + TAB_FOOTER + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader);

        try {

            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        PlayerConnection con = ((CraftPlayer)ply).getHandle().playerConnection;
        con.sendPacket(packet);

    }


}
