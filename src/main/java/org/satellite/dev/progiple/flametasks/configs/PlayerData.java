package org.satellite.dev.progiple.flametasks.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.satellite.dev.progiple.flametasks.FlameTasks;

import java.io.File;
import java.util.*;

public class PlayerData {
    @Getter
    private static final Map<String, PlayerData> data = new HashMap<>();
    private final Configuration config;

    public PlayerData(String nick) {
        File file = new File(FlameTasks.getPlugin().getDataFolder(), String.format("data/%s.yml", nick));
        this.config = new Configuration(file);
        if (!file.exists()) {
            List<String> emptyList = new ArrayList<>();
            for (String str : List.of("jack", "frank", "shaman")) {
                this.set(String.format("%s.collected_rewards", str), emptyList);
                this.set(String.format("%s.completed_quests", str), 0);
                this.set(String.format("%s.completed", str), 0);
            }

            this.save();
        }

        data.put(nick, this);
    }

    public void reload() {
        this.config.reload();
    }

    public void set(String path, Object object) {
        this.config.set(path, object);
    }

    public void save() {
        this.config.save();
    }

    public ConfigurationSection getSection(String id) {
        return this.config.getSection(id);
    }
}