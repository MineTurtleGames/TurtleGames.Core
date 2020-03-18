package co.turtlegames.core.achievement;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.achievement.action.FetchAchievementDataAction;
import co.turtlegames.core.achievement.action.FetchAchievementProgressAction;
import co.turtlegames.core.achievement.listener.WelcomeAchievementListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AchievementManager extends TurtleModule {

    private HashMap<Integer, MetaAchievement> _achievementData;

    public AchievementManager(JavaPlugin plugin) {

        super(plugin, "Achievement Manager");

        _achievementData = new HashMap<>();

    }

    @Override
    public void initializeModule() {

        FetchAchievementDataAction action = new FetchAchievementDataAction();
        Collection<MetaAchievement> fetchedAchievements;

        this.getDatabaseConnector().<Collection<MetaAchievement>>executeActionAsync(action)
                .thenAccept((Collection<MetaAchievement> achievements) -> {

                    for(MetaAchievement achievement : achievements) {
                        _achievementData.put(achievement.getId(), achievement);
                    }

                    System.out.println("Loaded " + achievements.size() + " achievements");

                });

        this.registerListener(new WelcomeAchievementListener(this));

    }

    public CompletableFuture<AchievementData> fetchAchievementData(UUID uuid) {

        CompletableFuture<AchievementData> toComplete = new CompletableFuture<>();

        CompletableFuture<Map<Integer,Integer>> dbFuture =
                this.getDatabaseConnector().executeActionAsync(new FetchAchievementProgressAction(uuid));

        dbFuture.thenAccept((Map<Integer, Integer> data) -> {

            AchievementData achievementData = new AchievementData(this, uuid);

            for(Map.Entry<Integer, Integer> entry : data.entrySet()) {

                achievementData.updateAchievementStatus(
                        new AchievementStatus(achievementData,
                                this.getAchievementById(entry.getKey()),
                                entry.getValue()));

            }

            toComplete.complete(achievementData);

        });

        dbFuture.exceptionally((Throwable ex) -> {
           toComplete.completeExceptionally(ex);
           return null;
        });

        return toComplete;

    }

    public MetaAchievement getAchievementById(Integer id) {
        return _achievementData.get(id);
    }

}
