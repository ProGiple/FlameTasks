package org.satellite.dev.progiple.flametasks.menus.buttons.realized;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.flametasks.configs.Config;
import org.satellite.dev.progiple.flametasks.configs.PlayerData;
import org.satellite.dev.progiple.flametasks.menus.buttons.Button;
import org.satellite.dev.progiple.flametasks.other.Status;
import org.satellite.dev.progiple.flametasks.other.TaskType;
import org.satellite.dev.progiple.flametasks.other.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class QuestButton extends Button {
    private final String nick;
    private final String taskGiver;
    private final String questId;
    private final ConfigurationSection taskSection;

    private Status status = Status.OPENED;
    @Setter private boolean needToReplacement = false;
    public QuestButton(ConfigurationSection section, String tier, String nick, String taskGiver) {
        super(section, section.getInt("slot"));
        this.nick = nick;
        this.taskGiver = taskGiver;
        this.questId = String.format("%s:%s", tier, section.getName());
        this.taskSection = section.getConfigurationSection("task");

        ConfigurationSection newSection = null;
        if (Tools.isCompletedTaskGiver(this.nick, this.taskGiver) || Tools.isBackQuest(this.nick, this.taskGiver, this.questId)) {
            this.status = Status.COMPLETED;
            newSection = Config.getSection("items.completed_quest");
        }
        else {
            String requiredQuestId = Tools.getQuestId(this.nick, this.taskGiver);
            if (requiredQuestId != null && !requiredQuestId.equals(this.questId)){
                this.status = Status.CLOSED;
                newSection = Config.getSection("items.closed_quest");
            }
        }

        if (newSection != null) {
            String strMaterial = newSection.getString("material");
            Material material = strMaterial == null ? Material.STONE : Material.getMaterial(strMaterial);
            this.setAll(material, 1, newSection.getString("displayName"),
                    newSection.getStringList("lore"), newSection.getBoolean("enchanted"));
            this.setDefaultLore(new ArrayList<>(this.getLore()));
        }
        this.updLore();
    }

    @Override
    public void click(Player player) {
        switch (this.status) {
            case OPENED -> {
                if (Objects.equals(this.taskSection.getString("type"), TaskType.PUT_ITEMS.name())) {
                    int max = this.taskSection.getInt("amount");
                    int reduce = Math.min(max - PlayerData.getData().get(this.nick)
                            .getSection(this.taskGiver).getInt("completed"), 64);

                    String strMaterial = this.taskSection.getString("value");
                    if (strMaterial == null) return;

                    Material material = Material.getMaterial(strMaterial);
                    assert material != null;
                    if (player.getInventory().contains(material, reduce)) {
                        player.getInventory().remove(new ItemStack(material, reduce));
                        if (!Tools.progressQuest(this.nick, TaskType.PUT_ITEMS,
                                (byte) reduce, strMaterial.split(";"))) {
                            player.sendMessage(Config.getMessage("giveItems").replace("{amount}", String.valueOf(reduce)));
                            this.updLore();
                            this.needToReplacement = true;
                        }
                        else player.closeInventory();
                    }
                    else player.sendMessage(Config.getMessage("noItems").replace("{amount}", String.valueOf(reduce)));
                }
                else player.sendMessage(Config.getMessage("questNotCompleted"));
            }
            case CLOSED -> player.sendMessage(Config.getMessage("questDisabled"));
            case COMPLETED -> player.sendMessage(Config.getMessage("questCompletedNow"));
        }
    }

    private void updLore() {
        List<String> lore = new ArrayList<>(this.getDefaultLore());

        int completed = PlayerData.getData().get(this.nick).getSection(this.taskGiver).getInt("completed");
        int max = this.taskSection.getInt("amount");
        lore.replaceAll(line -> line
                .replace("{completed}", String.valueOf(completed))
                .replace("{left}", String.valueOf(max - completed))
                .replace("{max}", String.valueOf(max)));
        this.setLore(lore);
    }

    public boolean getNeededToReInsert() {
        return this.needToReplacement;
    }
}