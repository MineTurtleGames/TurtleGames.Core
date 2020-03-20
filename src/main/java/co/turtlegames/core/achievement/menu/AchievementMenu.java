package co.turtlegames.core.achievement.menu;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.menu.page.RootAchievementPage;
import co.turtlegames.core.menu.Menu;
import co.turtlegames.core.profile.PlayerProfile;
import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AchievementMenu extends Menu<AchievementManager> {

    private PlayerProfile _playerProfile;

    public AchievementMenu(AchievementManager module, PlayerProfile playerProfile) {

        super(module, ChatColor.DARK_GRAY + "Achievements", playerProfile.getOwner());
        _playerProfile = playerProfile;

        this.addPage(new RootAchievementPage(this));

    }

    public PlayerProfile getPlayerProfile() {
        return _playerProfile;
    }

}
