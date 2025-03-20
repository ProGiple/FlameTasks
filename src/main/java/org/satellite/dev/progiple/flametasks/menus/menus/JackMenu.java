package org.satellite.dev.progiple.flametasks.menus.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.satellite.dev.progiple.flametasks.configs.JackConfig;
import org.satellite.dev.progiple.flametasks.menus.FlameMenu;

public class JackMenu extends FlameMenu {
    public JackMenu(Player player) {
        super(player, JackConfig.getString("title"),
                JackConfig.getInt("rows") * 9, JackConfig.getSection("items.decorations"), "jack");
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {}
}
