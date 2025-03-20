package org.satellite.dev.progiple.flametasks.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.satellite.dev.progiple.flametasks.FlameTasks;

import java.io.File;
import java.util.List;

@UtilityClass
public class FrankConfig {
    private final Configuration config;
    static {
        config = new Configuration(new File(FlameTasks.getPlugin().getDataFolder(), "taskGivers/frank.yml"));
    }

    public void reload() {
        config.reload();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public List<String> getList(String path) {
        return config.getStringList(path);
    }
}
