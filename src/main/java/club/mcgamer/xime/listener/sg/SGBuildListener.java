package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

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

                    switch (type) {
                        case LEAVES:
                        case LEAVES_2:
                        case CAKE_BLOCK:
                        case WEB:
                        case FIRE:
                        case LONG_GRASS:
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:
                        case VINE:
                        case WATER_LILY:
                        case RED_ROSE:
                            return;
                    }
            }
            event.getEvent().setCancelled(true);
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
                        case WATER_LILY:
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
