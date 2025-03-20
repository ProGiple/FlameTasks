package org.satellite.dev.progiple.flametasks.other;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.satellite.dev.progiple.flametasks.configs.*;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class Tools {
    public List<String> getOrderList(String taskGiver) {
        String orderPath = "quest_order";
        return taskGiver.equals("jack") ? JackConfig.getList(orderPath)
                : (taskGiver.equals("frank") ? FrankConfig.getList(orderPath) : ShamanConfig.getList(orderPath));
    }

    public boolean isCompletedTaskGiver(String nick, String taskGiver) {
        ConfigurationSection section = PlayerData.getData().get(nick).getSection(taskGiver);
        return section.getInt("completed_quests") >= getOrderList(taskGiver).size();
    }

    public Status getRewardStatus(String nick, String taskGiver, String tierId) {
        ConfigurationSection section = PlayerData.getData().get(nick).getSection(taskGiver);
        if (section.getStringList("collected_rewards").contains(tierId)) return Status.COMPLETED;

        int index = 0;
        List<String> order = getOrderList(taskGiver);
        for (int i = 0; i < order.size(); i++) {
            if (order.get(i).contains(tierId) && index < i) index = i;
        }
        return section.getInt("completed_quests") > index ? Status.OPENED : Status.CLOSED;
    }

    public String getNextTaskGiver(String nick) {
        for (String taskGiver : List.of("jack", "frank", "shaman")) {
            if (Tools.isCompletedTaskGiver(nick, taskGiver)) continue;
            return taskGiver;
        }
        return null;
    }

    public boolean isBackQuest(String nick, String taskGiver, String backQuestId) {
        PlayerData data = PlayerData.getData().get(nick);

        if (taskGiver != null) {
            int questIndex = data.getSection(taskGiver).getInt("completed_quests");
            List<String> order = Tools.getOrderList(taskGiver);
            if (order.size() > questIndex) {
                return order.indexOf(backQuestId) < questIndex;
            }
        }
        return false;
    }

    public String getQuestId(String nick, String taskGiver) {
        PlayerData data = PlayerData.getData().get(nick);

        if (taskGiver != null) {
            int questIndex = data.getSection(taskGiver).getInt("completed_quests");
            List<String> order = Tools.getOrderList(taskGiver);
            if (order.size() > questIndex) return order.get(questIndex);
        }
        return null;
    }

    public void completeQuest(String nick) {
        PlayerData data = PlayerData.getData().get(nick);
        String taskGiver = Tools.getNextTaskGiver(nick);
        if (taskGiver == null) return;

        data.set(String.format("%s.completed_quests", taskGiver),
                data.getSection(taskGiver).getInt("completed_quests") + 1);
        data.set(String.format("%s.completed", taskGiver), 0);
        data.save();

        Player player = Bukkit.getPlayerExact(nick);
        if (player != null) player.sendMessage(Config.getMessage("questCompleted"));
    }

    public boolean progressQuest(String nick, TaskType taskType, byte amount, String[] value) {
        PlayerData playerData = PlayerData.getData().get(nick);
        if (playerData == null) playerData = new PlayerData(nick);

        String taskGiver = Tools.getNextTaskGiver(nick);
        if (taskGiver == null) return false;

        String questId = Tools.getQuestId(nick, taskGiver);
        if (questId == null) return false;

        String path = "items.quests";
        ConfigurationSection questSection = taskGiver.equals("jack") ? JackConfig.getSection(path) :
                (taskGiver.equals("frank") ? FrankConfig.getSection(path) : ShamanConfig.getSection(path));

        String taskPath = String.format("%s.task", questId.replace(":", "."));
        if (!questSection.getKeys(false).contains(taskPath)) return false;
        ConfigurationSection taskSection = questSection.getConfigurationSection(taskPath);

        assert taskSection != null;
        if (Objects.equals(taskSection.getString("type"), taskType.name())) {
            String strValue = taskSection.getString("value");
            if (strValue != null) {
                String[] taskValue = strValue.split(";");
                if (value != null) {
                    if (!value[0].isEmpty() && !value[0].equals(taskValue[0])) {
                        return false;
                    } else if (value.length >= 2 && taskValue.length >= 2) {
                        if (Integer.parseInt(value[1]) < Integer.parseInt(taskValue[1])) {
                            return false;
                        }
                    }
                }
            }

            ConfigurationSection section = playerData.getSection(taskGiver);
            int newAmo = section.getInt("completed") + amount;
            if (newAmo >= taskSection.getInt("amount")) {
                Tools.completeQuest(nick);
                return true;
            }

            playerData.set(taskGiver + ".completed", newAmo);
            playerData.save();
        }
        return false;
    }
}
