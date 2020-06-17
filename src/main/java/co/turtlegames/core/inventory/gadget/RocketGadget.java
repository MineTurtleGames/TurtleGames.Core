package co.turtlegames.core.inventory.gadget;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class RocketGadget extends Gadget {

    public RocketGadget(UUID owner) {
        super(owner, "GADGET_ROCKET", "Rocket", 200, 5);
    }

    @Override
    public void onUse(Player player) {

        Vector velocity = player.getVelocity();
        velocity.setY(3);
        player.setVelocity(velocity);

    }

}