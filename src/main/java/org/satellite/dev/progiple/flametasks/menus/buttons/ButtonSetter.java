package org.satellite.dev.progiple.flametasks.menus.buttons;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.satellite.dev.progiple.flametasks.configs.FrankConfig;
import org.satellite.dev.progiple.flametasks.configs.JackConfig;
import org.satellite.dev.progiple.flametasks.configs.ShamanConfig;
import org.satellite.dev.progiple.flametasks.menus.buttons.realized.QuestButton;
import org.satellite.dev.progiple.flametasks.menus.buttons.realized.RewardButton;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonSetter {
    private final List<Button> buttons = new ArrayList<>();
    public ButtonSetter(String nick, String taskGiver) {
        ConfigurationSection itemsSection = taskGiver.equals("jack") ? JackConfig.getSection("items") :
                (taskGiver.equals("frank") ? FrankConfig.getSection("items") : ShamanConfig.getSection("items"));

        ConfigurationSection questSection = itemsSection.getConfigurationSection("quests");
        assert questSection != null;
        for (String tier : questSection.getKeys(false)) {
            ConfigurationSection tierSection = questSection.getConfigurationSection(tier);
            assert tierSection != null;
            tierSection.getKeys(false).forEach(quest -> {
                ConfigurationSection section = tierSection.getConfigurationSection(quest);
                this.buttons.add(new QuestButton(section, tier, nick, taskGiver));
            });
        }

        ConfigurationSection rewardSection = itemsSection.getConfigurationSection("rewards");
        assert rewardSection != null;
        for (String tier : rewardSection.getKeys(false)) {
            ConfigurationSection section = rewardSection.getConfigurationSection(tier);
            this.buttons.add(new RewardButton(section, nick, taskGiver));
        }
    }
}
