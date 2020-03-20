package co.turtlegames.core.achievement.menu.page;

import co.turtlegames.core.achievement.*;
import co.turtlegames.core.achievement.menu.AchievementMenu;
import co.turtlegames.core.achievement.unique.IAchievementUniqueReward;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import co.turtlegames.core.util.MenuCall;
import co.turtlegames.core.util.UtilString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.stream.Collectors;

public class AchievementCategoryPage extends Page<AchievementMenu> {

    private AchievementCategory _category;

    public AchievementCategoryPage(AchievementMenu menu, AchievementCategory category) {

        super(menu, 6);
        _category = category;

        this.addButton(0,  new ItemBuilder(Material.BED, ChatColor.DARK_GRAY + "... Back").build(),
                MenuCall.playSound(Sound.CLICK, MenuCall.wrap(this.getMenu()::openPreviousPage)));

        Collection<MetaAchievement> achievements = this.getMenu().getModule()
                                                            .getAchievements();

        Collection<MetaAchievement> gameAchievements = achievements.stream().filter(ach -> ach.getGame() == _category)
                                                            .collect(Collectors.toList());

        if(gameAchievements.size() == 0) {

            this.addButton(22, new ItemBuilder(Material.BARRIER, ChatColor.RED + "No achievements")
                                        .setLore(ChatColor.GRAY + "There are no achievements in this section").build());

            return;

        }

        this.addButton(10, new ItemBuilder(Material.CLAY_BALL, ChatColor.RED + "Loading...")
                                    .setLore(ChatColor.GRAY + "Please wait while your achievement data loads")
                                        .build());

        this.getMenu().getPlayerProfile().fetchAchievementData()
                .thenAccept((AchievementData achievementData) -> {

                    this.removeButton(10);

                    int slot = 10;
                    for (MetaAchievement ach : gameAchievements) {

                        AchievementStatus status = achievementData.getAchievementStatus(ach);

                        Material icon = Material.GLASS_BOTTLE;
                        String progress;

                        if (status.isComplete()) {

                            icon = Material.EXP_BOTTLE;
                            progress = ChatColor.GOLD + "Completed";

                        } else {
                            progress = ChatColor.GREEN.toString() + status.getProgress() + "/" + ach.getGoalValue();
                        }

                        String description = ach.getDescription();

                        if(ach.hasFlag(AchievementFlagType.HIDDEN_DESC) && !status.isComplete())
                            description = ChatColor.DARK_PURPLE + "This description is hidden";

                        String uniqueRewardLine = null;

                        if(ach.hasFlag(AchievementFlagType.UNIQUE_REWARD)) {

                            IAchievementUniqueReward reward = ach.getFlagData(AchievementFlagType.UNIQUE_REWARD);

                            if(reward != null) {
                                uniqueRewardLine = reward.getRewardLine();
                            }

                        }

                        ItemStack achievementStack = new ItemBuilder(icon, ChatColor.GREEN + ChatColor.BOLD.toString() + ach.getName())
                                .setLore(ChatColor.GRAY + description,
                                        "", ChatColor.GRAY + "Progress: " + progress,
                                        "",
                                        ChatColor.BLUE + "Rewards",
                                        ChatColor.AQUA + "+" + UtilString.formatInteger(ach.getRewardXp()) + " Experience",
                                        ChatColor.GOLD + "+" + UtilString.formatInteger(ach.getRewardCoins()) + " Coins",
                                        uniqueRewardLine
                                ).build();

                        this.addButton(slot, achievementStack);
                        slot++;

                    }

                });

    }
}
