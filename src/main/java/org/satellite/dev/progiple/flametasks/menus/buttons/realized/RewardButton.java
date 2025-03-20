package org.satellite.dev.progiple.flametasks.menus.buttons.realized;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.satellite.dev.progiple.flametasks.configs.Config;
import org.satellite.dev.progiple.flametasks.configs.PlayerData;
import org.satellite.dev.progiple.flametasks.menus.buttons.Button;
import org.satellite.dev.progiple.flametasks.other.Status;
import org.satellite.dev.progiple.flametasks.other.Tools;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RewardButton extends Button {
    private final Status status;
    private final String nick;
    private final String id;
    private final String taskGiver;
    private final List<String> commands;
    public RewardButton(ConfigurationSection section, String nick, String taskGiver) {
        super(section, section.getInt("slot"));
        this.nick = nick;
        this.id = section.getName();
        this.taskGiver = taskGiver;
        this.status = Tools.getRewardStatus(this.nick, this.taskGiver, this.id);
        this.commands = new ArrayList<>(section.getStringList("commands"));
        this.commands.replaceAll(line -> line.replace("{player}", this.nick));

        ConfigurationSection newSection = this.status == Status.CLOSED ? Config.getSection("items.closed_reward")
                : (this.status == Status.COMPLETED ? Config.getSection("items.collected_reward") : null);
        if (newSection != null) {
            String strMaterial = newSection.getString("material");
            Material material = strMaterial == null ? Material.STONE : Material.getMaterial(strMaterial);
            this.setAll(material, 1, newSection.getString("displayName"),
                    newSection.getStringList("lore"), newSection.getBoolean("enchanted"));
        }
    }

    @Override
    public void click(Player player) {
        switch (this.status) {
            case OPENED -> {
                this.commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                PlayerData playerData = PlayerData.getData().get(this.nick);

                List<String> collectedRewards = playerData.getSection(this.taskGiver).getStringList("collected_rewards");
                collectedRewards.add(this.id);
                playerData.set(String.format("%s.collected_rewards", this.taskGiver), collectedRewards);
                playerData.save();
                player.closeInventory();
            }
            case COMPLETED, CLOSED -> player.sendMessage(Config.getMessage("rewardNotCompleted"));
        }
    }
}
