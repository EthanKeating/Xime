package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class PhysicsListener extends IListener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        Material type = block.getType();
        if (type == Material.REDSTONE_WIRE ||
                type == Material.REDSTONE_TORCH_OFF ||
                type == Material.REDSTONE_TORCH_ON ||
                type == Material.REDSTONE_BLOCK ||
                type == Material.LEVER ||
                type == Material.STONE_BUTTON ||
                type == Material.WOOD_BUTTON ||
                type == Material.WOODEN_DOOR ||
                type == Material.IRON_DOOR ||
                type == Material.FENCE_GATE ||
                type == Material.TRAP_DOOR ||
                type == Material.DIODE_BLOCK_OFF || // Redstone repeater
                type == Material.DIODE_BLOCK_ON ||  // Redstone repeater
                type == Material.REDSTONE_LAMP_OFF ||
                type == Material.REDSTONE_LAMP_ON ||
                type == Material.DAYLIGHT_DETECTOR ||
                type == Material.DAYLIGHT_DETECTOR_INVERTED ||
                type == Material.REDSTONE_COMPARATOR ||
                type == Material.REDSTONE_COMPARATOR_OFF ||
                type == Material.REDSTONE_COMPARATOR_ON ||
                type == Material.HOPPER || // Hoppers are powered by redstone
                type == Material.PISTON_EXTENSION || // For pistons
                type == Material.PISTON_BASE ||
                type == Material.PISTON_STICKY_BASE ||
                type == Material.PISTON_MOVING_PIECE)
            return;

        event.setCancelled(true);
    }

}
