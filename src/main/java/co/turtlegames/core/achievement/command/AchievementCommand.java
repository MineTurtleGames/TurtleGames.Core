package co.turtlegames.core.achievement.command;

import co.turtlegames.core.achievement.AchievementManager;
import co.turtlegames.core.achievement.menu.AchievementMenu;
import co.turtlegames.core.command.CommandBase;
import co.turtlegames.core.common.Chat;
import co.turtlegames.core.profile.PlayerProfile;
import co.turtlegames.core.profile.Rank;

public class AchievementCommand extends CommandBase<AchievementManager> {

    public AchievementCommand(AchievementManager module) {
        super(module, Rank.PLAYER, "achievement", "achievements", "ach", "milestones");
    }

    @Override
    public void executeCommand(PlayerProfile profile, String[] args) {

        AchievementMenu menu = new AchievementMenu(this.getModule(), profile);
        menu.open();

    }



}
