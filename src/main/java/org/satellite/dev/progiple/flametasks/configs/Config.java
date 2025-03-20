package org.satellite.dev.progiple.flametasks.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.IConfig;
import org.novasparkle.lunaspring.Util.Utils;
import org.satellite.dev.progiple.flametasks.FlameTasks;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        config = new IConfig(FlameTasks.getPlugin());
    }

    public void reload() {
        config.reload(FlameTasks.getPlugin());
    }

    public String getMessage(String id) {
        return Utils.color(config.getString(String.format("messages.%s", id)));
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}
