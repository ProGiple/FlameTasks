package org.satellite.dev.progiple.flametasks.menus.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.satellite.dev.progiple.flametasks.configs.FrankConfig;
import org.satellite.dev.progiple.flametasks.menus.FlameMenu;

public class FrankMenu extends FlameMenu {
    public FrankMenu(Player player) {
        super(player, FrankConfig.getString("title"),
                FrankConfig.getInt("rows") * 9, FrankConfig.getSection("items.decorations"), "frank");
    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }
}
