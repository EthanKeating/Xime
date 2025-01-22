package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.loot.menu.SelectionChestMenu;
import club.mcgamer.xime.loot.tables.BadlionLootTable;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BGBuildListener extends IListener {

    @EventHandler
    private void onSGBreakBlock(ServerBreakBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable)event.getServerable();

            Material type = event.getBlock().getType();

            if (type == Material.FIRE)
                return;

            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onSGPlaceBlock(ServerPlaceBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();

            Block block = event.getBlock();
            Material type = block.getType();
            ItemStack heldItem = player.getItemInHand();
            Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

            switch (type) {
                case FIRE:
                    for(Location location : serverable.getSpawnLocations().values()) {
                        if (blockLocation.distance(location) < 2) {
                            event.getEvent().setCancelled(true);
                            return;
                        }
                    }

                    if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.FLINT_AND_STEEL)
                        return;

                    serverable.getPlacedBlocks().put(new MapLocation(blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ()), System.currentTimeMillis());
                    player.setItemInHand(new ItemStack(Material.AIR));
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
                    return;
            }
            event.getEvent().setCancelled(true);
        }
    }

}
