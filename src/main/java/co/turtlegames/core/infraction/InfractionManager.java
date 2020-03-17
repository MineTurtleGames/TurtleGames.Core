package co.turtlegames.core.infraction;

import co.turtlegames.core.TurtleCore;
import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.db.DatabaseConnector;
import co.turtlegames.core.infraction.action.FetchInfractionDataAction;
import co.turtlegames.core.infraction.action.RegisterInfractionAction;
import co.turtlegames.core.util.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InfractionManager extends TurtleModule  {

    private DatabaseConnector _dbConnector;

    public InfractionManager(TurtleCore plugin) {
        super(plugin, "Infraction Manager");

    }

    @Override
    public void initializeModule() {

        _dbConnector = this.getDatabaseConnector();

    }

    public CompletableFuture<InfractionData> fetchInfractionData(UUID uuid) {

        CompletableFuture<InfractionData> future = new CompletableFuture<>();

        FetchInfractionDataAction infractionDataFetch = new FetchInfractionDataAction(uuid);
        CompletableFuture<Collection<Infraction>> data =_dbConnector.executeActionAsync(infractionDataFetch);

        data.thenAccept((Collection<Infraction> infrData) -> {

            future.complete(new InfractionData(this, uuid, infrData));

        });

        data.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        return future;

    }

    public CompletableFuture<Boolean> registerInfraction(Infraction infraction) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        RegisterInfractionAction infractionDataFetch = new RegisterInfractionAction(infraction);
        CompletableFuture<Boolean> data =_dbConnector.executeActionAsync(infractionDataFetch);

        data.exceptionally((Throwable ex) -> {

            future.completeExceptionally(ex);
            return null;

        });

        data.thenAccept((Boolean success) -> {

            if(success)
                this.applyInfraction(infraction);

            future.complete(success);

        });

        return future;

    }

    private void applyInfraction(Infraction infraction) {

        System.out.println("hi");

        UUID uuid = infraction.getOwner();
        Player ply = Bukkit.getPlayer(uuid);

        if(ply == null)
            return;

        System.out.println("hi 2 ");

        ProfileManager profileManager = this.getModule(ProfileManager.class);

        System.out.println("hi 3");

        PlayerProfile profile;
        try {
            profile = profileManager.fetchProfile(uuid).get(15, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {

            ex.printStackTrace();
            ply.kickPlayer(ChatColor.RED + "An error occurred during modification of your profile.");

            return;

        }

        //System.out.println(ply.getUniqueId());

        ply.sendMessage("n word");

        ply.sendMessage(Chat.main("Infraction",
                "You have received a "
                        + Chat.elem(infraction.getType().getName())
                        + " with duration "
                        + Chat.elem("" + UtilString.formatTime(infraction.getDuration()))
                        + "\n\nReason: " + Chat.elem("doggy")));

        ply.playSound(ply.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);

        if(infraction.getType() == InfractionType.BAN)
            ply.kickPlayer("Get out buddy");

    }

}
