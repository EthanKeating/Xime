package club.mcgamer.xime.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.function.Predicate;

@UtilityClass
public class BlockUtil {

    public HashSet<Chunk> getChunksInRadius(Location location, int radius) {
        World world = location.getWorld();
        HashSet<Chunk> chunkSet = new HashSet<>();

        int currentChunkX = location.getChunk().getX();
        int currentChunkZ = location.getChunk().getZ();

        // Loop through the chunks in a square area around the player
        for (int xOffset = -radius; xOffset < radius; xOffset++) {
            for (int zOffset = -radius; zOffset < radius; zOffset++) {
                int chunkX = currentChunkX + xOffset;
                int chunkZ = currentChunkZ + zOffset;

                chunkSet.add(world.getChunkAt(chunkX, chunkZ));
            }
        }

        return chunkSet;
    }

    public HashSet<Block> getBlocks(Chunk chunk, Predicate<Block> condition) {
        HashSet<Block> blockSet = new HashSet<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 384; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);

                    if (condition.test(block))
                        blockSet.add(block);
                }
            }
        }
        return blockSet;
    }

    public HashSet<Block> getBlocks(Chunk chunk) {
        HashSet<Block> blockSet = new HashSet<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 384; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    blockSet.add(block);
                }
            }
        }
        return blockSet;
    }

    public HashSet<Block> getBlocksInSphere(Block centerBlock, int radius) {
        HashSet<Block> blocks = new HashSet<>();

        int centerX = centerBlock.getX();
        int centerY = centerBlock.getY();
        int centerZ = centerBlock.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        Block block = centerBlock.getWorld().getBlockAt(centerX + x, centerY + y, centerZ + z);

                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public boolean isImportantSphere(Block centerBlock, int radius) {

        int centerX = centerBlock.getX();
        int centerY = centerBlock.getY();
        int centerZ = centerBlock.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        Block block = centerBlock.getWorld().getBlockAt(centerX + x, centerY + y, centerZ + z);

                        if (isImportant(block))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isImportant(Block block) {
        Material type = block.getType();
        switch (type) {
            case AIR:
            case ANVIL:
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
            case TRAP_DOOR:
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case IRON_DOOR_BLOCK:
            case IRON_TRAPDOOR:
            case SIGN:
            case SIGN_POST:
            case GRAVEL:
            case SAND:
            case WALL_SIGN:
            case BED:
            case FENCE:
            case BANNER:
            case ICE:
            case PACKED_ICE:
            case STEP:
            case FENCE_GATE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case IRON_FENCE:
            case JUNGLE_FENCE:
            case SEEDS:
            case MELON_SEEDS:
            case WHEAT:
            case PUMPKIN_SEEDS:
            case NETHER_FENCE:
            case SPRUCE_FENCE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case SOIL:
            case PISTON_BASE:
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case PISTON_STICKY_BASE:
            case NOTE_BLOCK:
            case DISPENSER:
            case TNT:
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case HOPPER:
            case DROPPER:
            case STAINED_GLASS:
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case COBBLE_WALL:
            case WALL_BANNER:
            case WORKBENCH:
            case FURNACE:
            case BURNING_FURNACE:
            case CACTUS:
            case IRON_BARDING:
            case ENCHANTMENT_TABLE:
            case ENDER_PORTAL_FRAME:
            case SLIME_BLOCK:
            case DOUBLE_STEP:
            case WOOD_DOUBLE_STEP:
            case WOOD_DOOR:
            case SPRUCE_DOOR:
            case WOODEN_DOOR:
            case PAINTING:
            case ITEM_FRAME:
            case ARMOR_STAND:
            case BEACON:
            case CAULDRON:
            case HAY_BLOCK:
            case REDSTONE:
            case REDSTONE_BLOCK:
            case REDSTONE_COMPARATOR:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_LAMP_OFF:
            case REDSTONE_LAMP_ON:
            case REDSTONE_WIRE:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case SANDSTONE_STAIRS:
            case SMOOTH_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case ACACIA_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case BRICK_STAIRS:
            case COBBLESTONE_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case NETHER_BRICK_STAIRS:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case WOOD_STAIRS:
            case DOUBLE_STONE_SLAB2:
            case STONE_SLAB2:
            case YELLOW_FLOWER:
            case FLOWER_POT:
            case VINE:
            case LEAVES:
            case LEAVES_2:
            case LONG_GRASS:
            case GLASS:
            case SAPLING:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case WEB:
            case DEAD_BUSH:
            case RED_ROSE:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case FIRE:
            case CROPS:
            case LADDER:
            case RAILS:
            case LEVER:
            case STONE_BUTTON:
            case SNOW:
            case SUGAR_CANE_BLOCK:
            case PORTAL:
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case PUMPKIN_STEM:
            case MELON_STEM:
            case NETHER_WARTS:
            case ENDER_PORTAL:
            case COCOA:
            case TRIPWIRE_HOOK:
            case TRIPWIRE:
            case CARROT:
            case POTATO:
            case WOOD_BUTTON:
            case SKULL:
            case ACTIVATOR_RAIL:
            case CARPET:
            case DOUBLE_PLANT:
                return true;
        }
        return false;
    }

    public boolean isUseful(Block block) {
        Material type = block.getType();

        switch (type) {
            case ENDER_CHEST:
            case CHEST:
                return true;
            default:
                return false;
        }
    }
}
