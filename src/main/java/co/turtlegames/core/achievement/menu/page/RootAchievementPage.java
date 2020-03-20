package co.turtlegames.core.achievement.menu.page;

import co.turtlegames.core.achievement.AchievementCategory;
import co.turtlegames.core.achievement.menu.AchievementMenu;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RootAchievementPage extends Page<AchievementMenu> {

    public RootAchievementPage(AchievementMenu menu) {

        super(menu, ChatColor.DARK_GRAY + "Achievements", 6);

        int slot = 10;
        for(AchievementCategory category : AchievementCategory.values()) {

            String[] formattedDescription = new String[4 + category.getDescription().length];

            formattedDescription[0] = ChatColor.DARK_GRAY + "1/20 Complete";
            formattedDescription[1] = "";

            for(int i = 0; i < category.getDescription().length; i++)
                formattedDescription[2 + i] = ChatColor.GRAY + category.getDescription()[i];

            formattedDescription[2 + category.getDescription().length] = "";
            formattedDescription[2 + category.getDescription().length + 1] = ChatColor.GOLD + "Click to view";

            this.addButton(slot, new ItemBuilder(category.getIcon(), ChatColor.GREEN + category.getName())
                                        .setLore(formattedDescription)
                                            .build(), (page, event) -> {

                Player ply = this.getMenu().getOwner()
                                                .get();

                ply.playSound(ply.getLocation(), Sound.CLICK, 1, 1);
                this.getMenu().openDynamicPage(new AchievementCategoryPage(this.getMenu(), category));

            });

            slot++;

        }

    }


}
