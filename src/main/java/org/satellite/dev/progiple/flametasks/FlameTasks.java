package org.satellite.dev.progiple.flametasks;

import lombok.Getter;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.realized.NBTService;
import org.novasparkle.lunaspring.Util.managers.NBTManager;
import org.novasparkle.lunaspring.other.LunaPlugin;

public final class FlameTasks extends LunaPlugin {
    @Getter private static FlameTasks plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        this.loadFiles(true, "taskGivers/shaman.yml", "taskGivers/jack.yml", "taskGivers/frank.yml");

        NBTService nbtService = new NBTService();
        LunaSpring.getServiceProvider().register(nbtService);
        NBTManager.init(nbtService);

        this.initialize();
        this.registerListener(new Handler());
        this.registerTabExecutor(new Command(), "flametasks");
    }

    @Override
    public void onDisable() {
    }
}
