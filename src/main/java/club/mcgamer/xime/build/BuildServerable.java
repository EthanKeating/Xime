package club.mcgamer.xime.build;

import club.mcgamer.xime.build.input.InputType;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.util.BlockUtil;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.world.WorldHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
public class BuildServerable extends Serverable {

    private MapData mapData;
    private Profile editor;

    @Setter private InputType inputType = InputType.NONE;
    private List<Block> uselessBlocks = new ArrayList<>();

    public BuildServerable() {
        super();
    }

    @Override
    public TemporaryData createTemporaryData() {
        return TemporaryData.DEFAULT;
    }

    public void load(String bukkitWorld, Profile editor) {
        this.mapData = MapData.load(bukkitWorld);
        this.editor = editor;

        setWorld(bukkitWorld);
    }

    public void save() {
        WorldHandler worldHandler = plugin.getWorldHandler();
        String worldName = getWorld().getName();

        worldHandler.unload(getWorld(), true);
        worldHandler.convert(worldName, worldName);
    }

    public void discard() {
        WorldHandler worldHandler = plugin.getWorldHandler();

        worldHandler.unload(getWorld(), false);
    }

    public void updateChests() {
        mapData.getTier1Locations().clear();
        mapData.getTier2Locations().clear();

        World world = getWorld();
        Location centerLocation = mapData.getCenterLocation().toBukkit(world);
        final long startTime = System.currentTimeMillis();

        int centerChunkX = centerLocation.getChunk().getX();
        int centerChunkZ = centerLocation.getChunk().getZ();
        int radius = 16;

        for (int x = centerChunkX - radius; x < centerChunkX + radius; x++) {
            for (int z = centerChunkZ - radius; z < centerChunkZ + radius; z++) {
                Chunk chunk = world.getChunkAt(x, z);

                for (int blockX = 0; blockX < 16; blockX++) {
                    for (int blockZ = 0; blockZ < 16; blockZ++) {
                        for (int blockY = 0; blockY < 256; blockY++) {
                            Block block = chunk.getBlock(x * 16 + blockX, blockY, z * 16 + blockZ);
                            BlockState blockState = block.getState();
                            if (blockState.getType() == Material.ENDER_CHEST) {
                                mapData.getTier2Locations().add(MapLocation.fromBukkit(blockState.getLocation()));
                                continue;
                            }
                            if (blockState instanceof Chest) {
                                ((Chest) blockState).getBlockInventory().clear();
                                blockState.update();
                                mapData.getTier1Locations().add(MapLocation.fromBukkit(blockState.getLocation()));
                            }
                        }
                    }
                }
            }
        }
        editor.sendMessage(String.format("&8[&3Xime&8] &bFound &f%s &7&o(%s Tier 1s, %s Tier 2s)&f chests&b for &f%s &fin &e%sms",
                mapData.getTier2Locations().size() + mapData.getTier1Locations().size(),
                mapData.getTier1Locations().size(),
                mapData.getTier2Locations().size(),
                mapData.getMapName(),
                System.currentTimeMillis() - startTime));
    }

    @SneakyThrows
    public void optimize() {
        uselessBlocks = new ArrayList<>();
        World world = getWorld();
        Location centerLocation = mapData.getCenterLocation().toBukkit(world);

        int centerChunkX = centerLocation.getChunk().getX();
        int centerChunkZ = centerLocation.getChunk().getZ();
        int radius = 16;

        int chunkTotal = (radius * 2) * (radius * 2);

        LinkedList<Pair<Integer, Integer>> chunkCoordinates = new LinkedList<>();

        for (int x = centerChunkX - radius; x < centerChunkX + radius; x++)
            for (int z = centerChunkZ - radius; z < centerChunkZ + radius; z++)
                chunkCoordinates.add(new Pair<>(x, z));

        new BukkitRunnable() {
            int chunkCount = 0;

            public void run() {

                for(int i = 0; i < 10; i++) {
                    if (chunkCoordinates.isEmpty()) {
                        for (Block block : uselessBlocks)
                            block.setType(Material.AIR);
                        cancel();
                        editor.sendMessage(String.format("&8[&3Xime&8] &aChunk cleaner removed &6%s &auseless blocks", uselessBlocks.size()));
                        return;
                    }

                    Pair<Integer, Integer> chunkCoordinate = chunkCoordinates.pop();

                    Chunk chunk = world.getChunkAt(chunkCoordinate.getKey(), chunkCoordinate.getValue());

                    for (int blockX = 0; blockX < 16; blockX++) {
                        for (int blockZ = 0; blockZ < 16; blockZ++) {
                            for (int blockY = 0; blockY < 256; blockY++) {
                                Block block = chunk.getBlock(chunk.getX() * 16 + blockX, blockY, chunk.getZ() * 16 + blockZ);

                                if (!BlockUtil.isImportantSphere(block, 2))
                                    uselessBlocks.add(block);

                            }
                        }
                    }
                    if (++chunkCount % radius == 0)
                        editor.sendMessage(String.format("&8[&3Xime&8] &bChunk cleaner: " + chunkCount + "/" + chunkTotal));
                }
            }
        }.runTaskTimer(plugin, 20L, 1L);
    }
}
