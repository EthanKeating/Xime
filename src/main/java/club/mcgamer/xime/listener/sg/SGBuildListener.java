package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.loot.menu.SelectionChestMenu;
import club.mcgamer.xime.loot.tables.BadlionLootTable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SGBuildListener extends IListener {

    @EventHandler
    private void onSGBreakBlock(ServerBreakBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable)event.getServerable();

            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                return;
            }

            switch (serverable.getGameState()) {
                case LIVEGAME:
                case DEATHMATCH:
                    Material type = event.getBlock().getType();
                    GameSettings gameSettings = serverable.getGameSettings();

                    switch (type) {
                        case RED_ROSE:
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:

                            if (serverable.getGameSettings().isItemizedFlowers()) {
                                Block block = event.getBlock();

                                event.getEvent().setCancelled(true);
                                event.getEvent().getBlock().setType(Material.AIR);

                                Item item = block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), gameSettings.getLootTable().getRandomItem());
                                item.setPickupDelay(0);
                                item.setVelocity(new Vector(0, -0.08, 0));
                                return;
                            }
                        case LEAVES:
                        case LEAVES_2:
                        case CAKE_BLOCK:
                        case WEB:
                        case FIRE:
                        case LONG_GRASS:
                        case TNT:
                        case CAKE:
                        case ENDER_CHEST:
                        case VINE:
                            return;
                    }
            }
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onSelectionChestPlace(ServerPlaceBlockEvent event) {
        if (!(event.getServerable() instanceof SGServerable)) return;
        if (event.getBlock().getType() != Material.ENDER_CHEST) return;

        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        SGServerable serverable = (SGServerable)event.getServerable();

        ItemStack heldItem = player.getItemInHand();

        if (!heldItem.getItemMeta().hasDisplayName()) return;
        if (!heldItem.getItemMeta().getDisplayName().equals(BadlionLootTable.SELECTION_CHEST_NAME)) return;

        new SelectionChestMenu(
                serverable.getGameSettings().getLootTable().getSelectionChestItems(),
                event.getBlock().getLocation()
        ).open(player);

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onDynamitePlace(ServerPlaceBlockEvent event) {
        if (!(event.getServerable() instanceof SGServerable)) return;
        if (event.getBlock().getType() != Material.ENDER_CHEST) return;

        Material replaced = event.getEvent().getBlockReplacedState().getType();
        Material current = event.getBlock().getType();

        if (current == Material.TNT) {
            event.getBlock().setType(replaced);

            TNTPrimed primedTNT = (TNTPrimed) event.getBlock().getWorld().spawnEntity(
                    event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                    EntityType.PRIMED_TNT
            );

            primedTNT.setFuseTicks(30);
            primedTNT.setVelocity(new Vector(0, -0.08, 0));
        }
    }

    @EventHandler
    private void onSGPlaceBlock(ServerPlaceBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable)event.getServerable();

            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                return;
            }

            switch (serverable.getGameState()) {
                case LIVEGAME:
                case DEATHMATCH:
                    Material type = event.getBlock().getType();

                    switch (type) {
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:
                        case VINE:
                        case RED_ROSE:
                        case TNT:
                        case WEB:
                        case CAKE_BLOCK:
                        case DEAD_BUSH:
                        case FIRE:
                            return;

                    }
            }
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onFirePlace(ServerPlaceBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();

        if (!(profile.getServerable() instanceof SGServerable)) return;
        if (event.getEvent().isCancelled()) return;
        if (event.getBlock().getType() != Material.FIRE) return;
        if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.FLINT_AND_STEEL) return;

        ItemStack heldItem = player.getItemInHand();

        short nextDurability = (short) Math.min((short) (heldItem.getDurability() + (short) 16), (short)64);
        heldItem.setDurability(nextDurability);
        if (nextDurability > 63) {
            player.setItemInHand(new ItemStack(Material.AIR));
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
        }
        player.updateInventory();
    }
}
