package co.turtlegames.core.infraction.menu;

import co.turtlegames.core.infraction.InfractionManager;
import co.turtlegames.core.menu.Menu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class InfractionMenu extends Menu<InfractionManager> {

    private OfflinePlayer _target;
    private String _reason;

    public InfractionMenu(InfractionManager infractionManager, Player owner, OfflinePlayer target, String reason) {
        super(infractionManager, "Punish", owner);

        _target = target;
        _reason = reason;

        addPage(new InfractionPage(this));
    }

    public OfflinePlayer getTarget() {
        return _target;
    }

    public String getReason() {
        return _reason;
    }
}
