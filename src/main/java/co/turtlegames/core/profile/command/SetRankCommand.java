package co.turtlegames.core.profile.command;

import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.db.DatabaseException;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.ProfileManager;
import co.turtlegames.core.profile.Rank;
import co.turtlegames.core.scoreboard.TurtleScoreboardManager;
import io.netty.util.concurrent.CompleteFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.util.concurrent.CompletableFuture;

public class SetRankCommand extends CommandBase<ProfileManager> {

    public SetRankCommand(ProfileManager module) {

        super(module, Rank.ADMINISTRATOR, "setrank", "rank", "updaterank", "updaterank");

    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        Player ply = profile.getOwner();

        if(args.length < 2) {

            ply.sendMessage(Chat.main("Command", "Invalid arguments! Refer to: " + Chat.elem("/setrank <name> <rank>")));
            return;

        }

        @SuppressWarnings("deprecation")
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(target == null) {

            ply.sendMessage(Chat.main("Rank", "No player found under name '" + Chat.elem(args[0]) + "'"));
            return;

        }

        Rank toUpdateRank;

        try {
            toUpdateRank = Rank.valueOf(args[1].toUpperCase());
        } catch(IllegalArgumentException ex) {

            ply.sendMessage(Chat.main("Rank", "No rank found under name " + Chat.elem(args[1])));
            return;

        }

        if(toUpdateRank.isPermissible(Rank.ADMINISTRATOR)) {

            ply.sendMessage(Chat.main("Rank", "Ranks with permission levels at or above " + Chat.elem("Administrator") + " are unable to be assigned through this command"));
            return;

        }

        CompletableFuture<PlayerProfile> fetchFuture = this.getModule().fetchProfile(target.getUniqueId());

        fetchFuture.exceptionally((Throwable ex) -> {

            if(!(ex instanceof DatabaseException)) {

                ply.sendMessage(Chat.main("Profile", "An error occurred during profile retrieval"));
                return null;

            }

            DatabaseException dbEx = (DatabaseException) ex;

            if(dbEx.getFailureType() == DatabaseException.FailureType.NOT_FOUND){

                ply.sendMessage(Chat.main("Profile", "The player " + Chat.elem(target.getName()) + " does not have a registered profile"));
                return null;

            } else {

                ply.sendMessage(Chat.main("Profile", "An error occurred during profile retrieval"));
                return null;

            }

        });

        fetchFuture.thenAccept((PlayerProfile targetProfile) -> {

            if(targetProfile == null) {

                ply.sendMessage(Chat.main("Profile", "The player " + Chat.elem(target.getName()) + " does not have a registered profile"));
                return;

            }

            CompletableFuture<Boolean> rankUpdate = targetProfile.updateRank(toUpdateRank);

            rankUpdate.thenAccept((Boolean successful) -> {

                if(!successful) {
                    ply.sendMessage(Chat.main("Rank", "An error occurred while pushing rank update (unsuccessful)"));
                    return;
                }

                ply.sendMessage(Chat.main("Rank", Chat.elem(target.getName()) + "'s permission level was updated to " + Chat.elem(toUpdateRank.getName())));

                if(target.isOnline()) {

                    Player targetPly = target.getPlayer();

                    this.getModule().getModule(TurtleScoreboardManager.class)
                            .pokeAllTeamData(targetPly);

                    targetPly.sendMessage(Chat.main("Rank", "Your permission level was updated to " + Chat.elem(toUpdateRank.getName())));

                }

            });

            rankUpdate.exceptionally((Throwable ex) -> {

                ex.printStackTrace();

                ply.sendMessage(Chat.main("Rank", "An error occurred while pushing rank update"));
                return null;

            });

        });

    }

}
