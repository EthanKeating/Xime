package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.loot.menu.SelectionChestMenu;
import club.mcgamer.xime.loot.tables.BadlionLootTable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
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
                        case DEAD_BUSH:
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
    private void onDynamitePlace(ServerPlaceBlockEvent event) {
        if (!(event.getServerable() instanceof SGServerable)) return;
        if (event.getBlock().getType() != Material.TNT) return;

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
    private void onDynamiteExplosion(EntityExplodeEvent event) {
        Location dynamiteExplosion = event.getEntity().getLocation();
        event.setCancelled(true);
        event.blockList().clear();
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
                    ItemStack heldItem = player.getItemInHand();

                    switch (type) {
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:
                        case VINE:
                        case RED_ROSE:
                        case TNT:
                        case WEB:
                        case CAKE_BLOCK:
                        case DEAD_BUSH:
                        case ENDER_CHEST:

                            if (!heldItem.getItemMeta().hasDisplayName()) return;
                            if (!heldItem.getItemMeta().getDisplayName().equals(BadlionLootTable.SELECTION_CHEST_NAME)) return;

                            new SelectionChestMenu(
                                    serverable.getGameSettings().getLootTable().getSelectionChestItems(),
                                    event.getBlock().getLocation()
                            ).open(player);
                            return;
                        case FIRE:
                            if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.FLINT_AND_STEEL) return;

                            short nextDurability = (short) Math.min((short) (heldItem.getDurability() + (short) 16), (short)64);
                            heldItem.setDurability(nextDurability);
                            if (nextDurability > 63) {
                                player.setItemInHand(new ItemStack(Material.AIR));
                                player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
                            }
                            return;
                    }
            }
            event.getEvent().setCancelled(true);
        }
    }
}
