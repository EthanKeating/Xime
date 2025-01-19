package club.mcgamer.xime.build;

import club.mcgamer.xime.build.design.BuildSidebarAdapter;
import club.mcgamer.xime.build.input.InputType;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.util.BlockUtil;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.world.WorldHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
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

    private final BukkitRunnable displayRunnable;

    public BuildServerable() {
        super();

        setSidebarAdapter(new BuildSidebarAdapter());

        displayRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                displayLocations();
            }
        };

        displayRunnable.runTaskTimer(getPlugin(), 1L, 1L);
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

    public void displayLocations() {
        if (getWorld() == null) return;

        List<Location> spawnLocations = mapData.getSpawnLocations().stream()
                .map(mapLocation -> mapLocation.toBukkit(getWorld()))
                .toList();

        List<Location> dmLocations = mapData.getDmLocations().stream()
                .map(mapLocation -> mapLocation.toBukkit(getWorld()))
                .toList();

        Location mapCenter = mapData.getCenterLocation() == null ? null : mapData.getCenterLocation().toBukkit(getWorld());
        Location specLocation = mapData.getSpectateLocation() == null ? null : mapData.getSpectateLocation().toBukkit(getWorld());
        Location dmCenterLocation = mapData.getDmCenterLocation() == null ? null : mapData.getDmCenterLocation().toBukkit(getWorld());

        getPlayerList().forEach(profile -> {
            Player player = profile.getPlayer();

            for(Location spawnLocation : spawnLocations) {

                PacketPlayOutWorldParticles spawnLocationPacket = new PacketPlayOutWorldParticles(
                        EnumParticle.REDSTONE,
                        true,
                        (float) spawnLocation.getX(),
                        (float) spawnLocation.getY() + 0.1f,
                        (float) spawnLocation.getZ(),
                        0f / 255f,
                        255f / 255f,
                        255f / 255f,
                        (float) 1,
                        0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(spawnLocationPacket);
            }

            for(Location dmLocation : dmLocations) {

                PacketPlayOutWorldParticles dmLocationPacket = new PacketPlayOutWorldParticles(
                        EnumParticle.REDSTONE,
                        true,
                        (float) dmLocation.getX(),
                        (float) dmLocation.getY() + 0.1f,
                        (float) dmLocation.getZ(),
                        250f / 255f,
                        128f / 255f,
                        114f / 255f,
                        (float) 1,
                        0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(dmLocationPacket);
            }

            if (mapCenter != null) {
                PacketPlayOutWorldParticles centerLocationPacket = new PacketPlayOutWorldParticles(
                        EnumParticle.REDSTONE,
                        true,
                        (float) mapCenter.getX(),
                        (float) mapCenter.getY() + 0.1f,
                        (float) mapCenter.getZ(),
                        50f / 255f,
                        205f / 255f,
                        50f / 255f,
                        (float) 1,
                        0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerLocationPacket);
            }

            if (specLocation != null) {
                PacketPlayOutWorldParticles specLocationPacket = new PacketPlayOutWorldParticles(
                        EnumParticle.REDSTONE,
                        true,
                        (float) specLocation.getX(),
                        (float) specLocation.getY() + 0.1f,
                        (float) specLocation.getZ(),
                        211f / 255f,
                        211f / 255f,
                        211f / 255f,
                        (float) 1,
                        0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(specLocationPacket);
            }

            if (dmCenterLocation != null) {
                PacketPlayOutWorldParticles dmLocationPacket = new PacketPlayOutWorldParticles(
                        EnumParticle.REDSTONE,
                        true,
                        (float) dmCenterLocation.getX(),
                        (float) dmCenterLocation.getY() + 0.1f,
                        (float) dmCenterLocation.getZ(),
                        95f / 255f,
                        95f / 255f,
                        95f / 255f,
                        (float) 1,
                        0);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(dmLocationPacket);
            }

        });
    }

    public void save() {
        WorldHandler worldHandler = plugin.getWorldHandler();
        String worldName = getWorld().getName();

        if (displayRunnable != null)
            displayRunnable.cancel();

        worldHandler.unload(getWorld(), true);
        worldHandler.convert(worldName, worldName);
    }

    public void discard() {
        WorldHandler worldHandler = plugin.getWorldHandler();

        if (displayRunnable != null)
            displayRunnable.cancel();

        worldHandler.unload(getWorld(), false);
    }

    public void updateChests(int radius) {
        mapData.getTier1Locations().clear();
        mapData.getTier2Locations().clear();

        World world = getWorld();
        Location centerLocation = mapData.getCenterLocation().toBukkit(world);
        final long startTime = System.currentTimeMillis();

        int centerChunkX = centerLocation.getChunk().getX();
        int centerChunkZ = centerLocation.getChunk().getZ();

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
    public void optimize(int radius) {
        MapData.save(getWorldName(), mapData);

        World world = getWorld();
        Location centerLocation = mapData.getCenterLocation().toBukkit(world);

        int centerChunkX = centerLocation.getChunk().getX();
        int centerChunkZ = centerLocation.getChunk().getZ();

        int chunkTotal = (radius * 2) * (radius * 2);

        LinkedList<Pair<Integer, Integer>> chunkCoordinates = new LinkedList<>();
        LinkedList<Block> uselessBlocks = new LinkedList<>();

        for (int x = centerChunkX - radius; x < centerChunkX + radius; x++)
            for (int z = centerChunkZ - radius; z < centerChunkZ + radius; z++)
                chunkCoordinates.add(new Pair<>(x, z));

        new BukkitRunnable() {
            int chunkCount = 0;

            public void run() {

                for(int i = 0; i < 10; i++) {
                    if (chunkCoordinates.isEmpty()) {
                        editor.sendMessage("&8[&3Xime&8] &cChunk cleaner removing &e" + uselessBlocks.size());

                        for (Block block : uselessBlocks)
                            BlockUtil.setBlockInNativeWorld(getWorld(), block.getX(), block.getY(), block.getZ(), 0, (byte)0, false);

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

    public void mainItems(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(0,
                new ItemBuilder(Material.NAME_TAG)
                        .name("&6#1 &eName Data &c(Required)")
                        .lore("&f#1 &7Set the map's name", "&f#1 &7Set the map's author(s)", "&f#1 &7Set the map's link")
                        .build());
        player.getInventory().setItem(1,
                new ItemBuilder(Material.SKULL_ITEM)
                        .data(4)
                        .name("&6#2 &eSet Map Center &c(Required)")
                        .lore("&aPlace this head at the center of the map", "&cClick air with this item to remove the map center.")
                        .build());
        player.getInventory().setItem(2,
                new ItemBuilder(Material.SKULL_ITEM)
                        .data(3)
                        .name("&6#3 &eSet Pedestal Locations &c(Required)")
                        .lore("&aPlace this head at each pedistal location", "&cClick air with this item to remove the previous spawn.")
                        .build());
        player.getInventory().setItem(3,
                new ItemBuilder(Material.CHEST)
                        .name("&6#4 &eSet Chests &c(Required)")
                        .lore("&7Please ensure that every Tier II is set as an enderchest.")
                        .build());
        player.getInventory().setItem(4,
                new ItemBuilder(Material.SKULL_ITEM)
                        .name("&6#4 &eSet Spectator Spawn &a(Optional)")
                        .lore("&7If you choose to skip this step", "&7he map center will become the spectator spawn.")
                        .build());
        player.getInventory().setItem(5,
                new ItemBuilder(Material.SKULL_ITEM)
                        .data(1)
                        .name("&6#4 &eSet Deathmatch Center &a(Optional)")
                        .lore("&7If you choose to skip this step, the", "&7map center will become the deathmatch center.")
                        .build());
        player.getInventory().setItem(6,
                new ItemBuilder(Material.SKULL_ITEM)
                        .data(2)
                        .name("&6#4 &eSet Deathmatch Locations &a(Optional)")
                        .lore("&7If you choose to skip this step the", "&7map pedistal locations will become the deathmatch locations.")
                        .build());
        player.getInventory().setItem(7,
                new ItemBuilder(Material.DEAD_BUSH)
                        .data(2)
                        .name("&6#4 &eOptimize Map &4(Warning)")
                        .lore("&7This feature scans through the entire map",
                                "&7and removes every block that cannot be seen by players",
                                "&7It is used for client & server optimizations, along",
                                "&7with allowing the Bossbar to function better!",
                                "",
                                "&aThis option NOT REQUIRED but is HIGHLY RECOMMENDED",
                                "&cBUT, this feature WILL cause serverwide lag & will take some time to complete",
                                "&cThere is a chance this could also crash the server",
                                "&4&lUSE AT YOUR OWN RISK ON A WHITELISTED SERVER")
                        .build());

        player.getInventory().setItem(8,
                new ItemBuilder(Material.BARRIER)
                        .name("&6Save / Discard Map Changes &c(Required)")
                        .lore("&7Choose whether you want to save or discard the current map changes")
                        .build());

    }
}
