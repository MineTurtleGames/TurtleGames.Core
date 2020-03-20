package co.turtlegames.core.util;

import co.turtlegames.core.menu.IButtonCallback;
import co.turtlegames.core.menu.Page;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MenuCall {

    public interface VoidProcedure {
        public void invoke();
    }

    public static IButtonCallback<Page> wrap(VoidProcedure voidProcedure) {

        return (page, event) -> voidProcedure.invoke();

    }

    public static IButtonCallback playSound(Sound sound, IButtonCallback<Page> callback) {

        return (page, event) -> {

            Player ply = (Player) event.getWhoClicked();
            ply.playSound(ply.getLocation(), sound, 1, 1);

            callback.onClick(page, event);

        };

    }

}
