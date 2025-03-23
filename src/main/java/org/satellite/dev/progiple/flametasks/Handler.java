package org.satellite.dev.progiple.flametasks;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.satellite.dev.progiple.flametasks.other.TaskType;
import org.satellite.dev.progiple.flametasks.other.Tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Handler implements Listener {
    private final Set<Material> seedMaterials = new HashSet<>(List.of(
            Material.WHEAT, Material.WHEAT_SEEDS, Material.CARROTS, Material.POTATOES,
            Material.MELON, Material.PUMPKIN, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS,
            Material.BEETROOT_SEEDS, Material.BEETROOTS));
    @EventHandler @SuppressWarnings("deprecation")
    public void onBreak(BlockBreakEvent e) {
        String nick = e.getPlayer().getName();
        Block block = e.getBlock();

        TaskType taskType = TaskType.BREAK_BLOCK;
        Material material = block.getType();
        String[] value = material.name().split(";");
        if (this.seedMaterials.contains(material)) {
            taskType = TaskType.COLLECT_SEEDS;
            value = new String[]{material.name(), String.valueOf(block.getData())};
        }
        Tools.progressQuest(nick, taskType, (byte) 1, value);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        String nick = e.getPlayer().getName();
        Tools.progressQuest(nick, TaskType.PLACE_BLOCK, (byte) 1, e.getBlock().getType().name().split(";"));
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        Player player = e.getEntity().getKiller();
        if (player == null) return;
        Tools.progressQuest(player.getName(), TaskType.KILL_ENTITY, (byte) 1, entity.getType().name().split(";"));
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        Player player = e.getEnchanter();
        Tools.progressQuest(player.getName(), TaskType.ENCHANT_ITEMS, (byte) 1, e.getItem().getType().name().split(";"));
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        ItemStack currect = e.getCurrentItem();
        if (currect == null) return;

        Player player = (Player) e.getWhoClicked();
        Tools.progressQuest(player.getName(), TaskType.CRAFT_ITEMS,
                (byte) currect.getAmount(), e.getRecipe().getResult().getType().name().split(";"));
    }

    @EventHandler
    public void onGetItemsFromFurnace(FurnaceExtractEvent e) {
        Player player = e.getPlayer();
        Tools.progressQuest(player.getName(), TaskType.GET_ITEMS_FROM_FURNACE,
                (byte) e.getItemAmount(), e.getItemType().name().split(";"));
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player player = e.getPlayer();
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Entity entity = e.getCaught();
            if (!(entity instanceof Item)) return;

            ItemStack item = ((Item) entity).getItemStack();
            Tools.progressQuest(player.getName(), TaskType.FISH, (byte) 1, item.getType().name().split(";"));
        }
    }

    @EventHandler
    public void onBreakItem(PlayerItemBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack itemStack = e.getBrokenItem();
        Tools.progressQuest(player.getName(), TaskType.BREAK_ITEMS, (byte) 1, itemStack.getType().name().split(";"));
    }

    @EventHandler
    public void onConsumeItem(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        Tools.progressQuest(player.getName(), TaskType.CONSUME_ITEMS, (byte) 1, e.getItem().getType().name().split(";"));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!e.hasChangedPosition()) return;

        String value = "foot";
        if (player.isSprinting()) value = "run";
        else if (player.isSneaking()) value = "sneaking";
        else if (player.isGliding()) value = "elytra";
        else if (player.isFlying()) value = "fly";
        else if (player.isSwimming()) value = "swim";

        int pos = (int) e.getFrom().distance(e.getTo());
        Tools.progressQuest(player.getName(), TaskType.MOVE, (byte) pos, value.split(";"));
    }

    @EventHandler
    public void onChangeExp(PlayerExpChangeEvent e) {
        int amount = e.getAmount();
        if (amount < 0) return;
        Tools.progressQuest(e.getPlayer().getName(), TaskType.EXP_CHANGE, (byte) amount, null);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        Tools.progressQuest(player.getName(), TaskType.REGAIN_HEALTH, (byte) e.getAmount(), null);
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onShootBow(EntityShootBowEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        ItemStack bow = e.getBow();
        if (bow == null) return;

        ItemStack projectile = e.getArrowItem();
        Material material = bow.getType();

        String value = String.format("%s;%s", material.name(), projectile.getType().name());
        Tools.progressQuest(player.getName(), TaskType.SHOOT, (byte) 1, value.split(";"));
    }

    @EventHandler
    public void onLoadCrossbow(EntityLoadCrossbowEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        if (e.getCrossbow() == null) return;

        Tools.progressQuest(player.getName(), TaskType.LOAD_CROSSBOW, (byte) 1, null);
    }
}
