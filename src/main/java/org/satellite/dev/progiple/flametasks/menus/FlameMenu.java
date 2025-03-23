package org.satellite.dev.progiple.flametasks.menus;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.AMenu;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.satellite.dev.progiple.flametasks.configs.Config;
import org.satellite.dev.progiple.flametasks.configs.PlayerData;
import org.satellite.dev.progiple.flametasks.menus.buttons.Button;
import org.satellite.dev.progiple.flametasks.menus.buttons.ButtonSetter;
import org.satellite.dev.progiple.flametasks.menus.buttons.realized.QuestButton;
import org.satellite.dev.progiple.flametasks.menus.menus.FrankMenu;
import org.satellite.dev.progiple.flametasks.menus.menus.JackMenu;
import org.satellite.dev.progiple.flametasks.menus.menus.ShamanMenu;
import org.satellite.dev.progiple.flametasks.other.Tools;

@Getter
public abstract class FlameMenu extends AMenu {
    public static void openMenu(Player player, String taskGiver) {
        FlameMenu menu;
        if (!PlayerData.getData().containsKey(player.getName())) new PlayerData(player.getName());
        if (!taskGiver.equals("jack")) {
            String nick = player.getName();
            if (taskGiver.equals("shaman") && Tools.isCompletedTaskGiver(nick, "frank")) menu = new ShamanMenu(player);
            else if (taskGiver.equals("frank") && Tools.isCompletedTaskGiver(nick, "jack")) menu = new FrankMenu(player);
            else {
                player.sendMessage(Config.getMessage("uncompletedTaskGiver"));
                return;
            }
        }
        else menu = new JackMenu(player);
        MenuManager.openInventory(player, menu);
    }

    private final ButtonSetter buttonSetter;
    public FlameMenu(Player player, String title, int size, ConfigurationSection decorSection, String taskGiver) {
        super(player, title, (byte) size, decorSection);

        if (!PlayerData.getData().containsKey(player.getName())) new PlayerData(player.getName());
        this.buttonSetter = new ButtonSetter(this.getPlayer().getName(), taskGiver);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.buttonSetter.getButtons().forEach(this::addItems);
        this.insertAllItems();
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        e.setCancelled(true);
        for (Button button : this.buttonSetter.getButtons()) {
            if (button.getItemStack().equals(item)) {
                button.click(this.getPlayer());
                if (button instanceof QuestButton) {
                    QuestButton questButton = (QuestButton) button;
                    if (questButton.getNeededToReInsert()) {
                        questButton.setNeedToReplacement(false);
                        questButton.insert(this);
                    }
                }
                return;
            }
        }
    }
}
