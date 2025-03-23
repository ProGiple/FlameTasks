package org.satellite.dev.progiple.flametasks;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.satellite.dev.progiple.flametasks.configs.*;
import org.satellite.dev.progiple.flametasks.menus.FlameMenu;

import java.util.List;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length >= 1) {
            switch (strings[0]) {
                case "reload" -> {
                    if (commandSender.hasPermission("flametasks.reload")) {
                        Config.reload();
                        JackConfig.reload();
                        FrankConfig.reload();
                        ShamanConfig.reload();
                        PlayerData.getData().values().forEach(PlayerData::reload);
                        commandSender.sendMessage(Config.getMessage("reload"));
                    }
                    else commandSender.sendMessage(Config.getMessage("noPermission"));
                }
                case "frank", "jack", "shaman" -> {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        FlameMenu.openMenu(player, strings[0]);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) return List.of("reload", "frank", "jack", "shaman");
        return List.of();
    }
}
