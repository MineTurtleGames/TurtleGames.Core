package co.turtlegames.core.profile.listeners;

import co.turtlegames.core.common.Chat;
import co.turtlegames.core.infraction.Infraction;
import co.turtlegames.core.infraction.InfractionData;
import co.turtlegames.core.infraction.InfractionType;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.concurrent.*;

public class ProfilePreCacheListener implements Listener {

    private ProfileManager _profileManager;

    public ProfilePreCacheListener(ProfileManager profileManager) {
        _profileManager = profileManager;
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {

        UUID uuid = event.getUniqueId();
        CompletableFuture<PlayerProfile> profileFuture = _profileManager.fetchProfile(uuid);

        PlayerProfile profile;
        try {

            profile = profileFuture.get();
            profile.fetchInfractionData().get(5, TimeUnit.SECONDS);

        } catch(InterruptedException | ExecutionException | TimeoutException ex) {

            Throwable cause = ex;

            if(cause.getCause() != null)
                cause = cause.getCause();

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Failed to fetch your profile\n\n"
                    + ChatColor.GRAY + cause.getMessage());

            return;

        }

        InfractionData infractionData = profile.getInfractionData();
        Infraction infraction = infractionData.getRelevantInfraction(InfractionType.BAN);

        if(infraction != null) {

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Chat.getBanMessage(infraction));
            return;

        }

        if(!profile.canConnect()) {

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You are unable to connect to this server");
            return;

        }

        profile.fetchAchievementData();
        profile.fetchCurrencyData();
        profile.fetchStatData();

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player ply = event.getPlayer();

        UUID uuid = ply.getUniqueId();
        CompletableFuture<PlayerProfile> profileFuture = _profileManager.fetchProfile(uuid);

        PlayerProfile profile;
        try {
            profile = profileFuture.get();
        } catch(InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return;
        }

        Rank rank = profile.getRank();

        event.setJoinMessage(ChatColor.GREEN + "+ "
                                + rank.getTag() + (rank == Rank.PLAYER ? "" : " ")
                                + ChatColor.WHITE + ply.getDisplayName());

    }

}

