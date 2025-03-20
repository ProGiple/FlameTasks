package org.satellite.dev.progiple.flametasks;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.NBTService;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.Util.Utils;
import org.novasparkle.lunaspring.Util.managers.NBTManager;

public final class FlameTasks extends JavaPlugin {
    @Getter private static FlameTasks plugin;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveResource("taskGivers/frank.yml", false);
        plugin.saveResource("taskGivers/jack.yml", false);
        plugin.saveResource("taskGivers/shaman.yml", false);
        saveDefaultConfig();

        NBTService nbtService = new NBTService();
        LunaSpring.getServiceProvider().register(nbtService);
        NBTManager.init(nbtService);

        this.reg(new MenuHandler());
        this.reg(new Handler());

        PluginCommand pluginCommand = plugin.getCommand("flametasks");
        if (pluginCommand != null) {
            Command command = new Command();
            pluginCommand.setTabCompleter(command);
            pluginCommand.setExecutor(command);
        }
    }

    @Override
    public void onDisable() {
    }

    private void reg(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
