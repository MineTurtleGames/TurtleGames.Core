package co.turtlegames.core.recharge;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class RechargeManager extends TurtleModule {

    private HashMap<String, RechargeToken> _cooldowns;

    public RechargeManager(JavaPlugin plugin) {
        super(plugin, "Recharge Manager");

        _cooldowns = new HashMap<>();
    }

    @Override
    public void initializeModule() {

    }

    public void startCooldown(Player player, String key, double seconds) {
        startCooldown(player, key, seconds, false);
    }

    public void startCooldown(Player player, String key, double seconds, boolean notify) {

        long millis = (long) seconds * 1000;
        RechargeToken token = new RechargeToken(player.getUniqueId(), millis);

        if (notify) {

            Bukkit.getScheduler().runTaskLater(getPlugin(), new BukkitRunnable() {

                @Override
                public void run() {

                    player.sendMessage(Chat.main(getName(), "You can use " + Chat.elem(key) + " again."));

                }

            }, millis);

        }

        _cooldowns.put(key, token);

    }

    public boolean canUse(Player player, String key) {
        return canUse(player, key, true);
    }

    public boolean canUse(Player player, String key, boolean message) {

        UUID uuid = player.getUniqueId();

        if (!_cooldowns.containsKey(uuid))
            return true;

        RechargeToken token = _cooldowns.get(uuid);

        if (token.hasFinished()) {

            _cooldowns.remove(uuid);
            return true;

        }

        // TODO format
        long waitTime = System.currentTimeMillis() - token.getFinishTime();

        player.sendMessage(Chat.main(getName(), "You need to wait " + Chat.elem(waitTime + "") + " before you can use " + Chat.elem(key) + " again."));

        return false;

    }

}
