package org.satellite.dev.progiple.flametasks.menus.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.satellite.dev.progiple.flametasks.configs.ShamanConfig;
import org.satellite.dev.progiple.flametasks.menus.FlameMenu;

public class ShamanMenu extends FlameMenu {
    public ShamanMenu(Player player) {
        super(player, ShamanConfig.getString("title"),
                ShamanConfig.getInt("rows") * 9, ShamanConfig.getSection("items.decorations"), "shaman");
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {}
}
