package club.mcgamer.xime.listener.build;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.build.input.InputType;
import club.mcgamer.xime.build.menu.ChestMenu;
import club.mcgamer.xime.build.menu.MapDataMenu;
import club.mcgamer.xime.build.menu.MapSaveMenu;
import club.mcgamer.xime.build.menu.OptimizeMenu;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import com.github.retrooper.packetevents.util.Vector3i;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class BuildItemInteractListener extends IListener {

    @EventHandler
    private void onBuildInteract(ServerItemInteractEvent event) {
        if (event.getServerable() instanceof BuildServerable) {
            BuildServerable serverable = (BuildServerable) event.getServerable();

            World world = serverable.getWorld();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            MapData mapData = serverable.getMapData();

            ItemStack item = event.getItemStack();

            if (!item.getItemMeta().hasLore()) return;
            event.getEvent().setCancelled(true);

            if (item.getType() == Material.NAME_TAG) {
                new MapDataMenu(profile, serverable).open(player);
                return;
            }
            if (item.getType() == Material.BARRIER) {
                new MapSaveMenu(profile, serverable).open(player);
                return;
            }

            if (item.getType() == Material.CHEST) {
                new ChestMenu(profile, serverable).open(player);
                return;
            }
            if (item.getType() == Material.DEAD_BUSH) {
                new OptimizeMenu(profile, serverable).open(player);
                return;
            }

            Block block = event.getEvent().getClickedBlock();

            if (item.getType() == Material.SKULL_ITEM) {
                if (item.getDurability() == 4) {
                    if (block == null) {
                        mapData.setCenterLocation(new MapLocation(0, 64, 0));
                        world.setSpawnLocation(0, 64, 0);

                        profile.sendMessage("&8[&3Xime&8] &cRemoved &fMap Center &bfrom map &f" + mapData.getMapName());
                        return;
                    }

                    Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());
                    mapData.setCenterLocation(new MapLocation(blockLocation.getX(), blockLocation.getY() + 1.0, blockLocation.getZ()));
                    world.setSpawnLocation(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fMap Center &bfor map &f" + mapData.getMapName());

                    return;
                }
                if (item.getDurability() == 1) {
                    if (block == null) {
                        mapData.setDmCenterLocation(null);

                        profile.sendMessage("&8[&3Xime&8] &cRemoved &fDeathmatch Center &bfrom map &f" + mapData.getMapName());
                        return;
                    }

                    Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());
                    mapData.setDmCenterLocation(new MapLocation(blockLocation.getX(), blockLocation.getY() + 1.0, blockLocation.getZ()));
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fDeathmatch Center &bfor map &f" + mapData.getMapName());

                    return;
                }
                if (item.getDurability() == 0) {
                    if (block == null) {
                        mapData.setSpectateLocation(null);

                        profile.sendMessage("&8[&3Xime&8] &cRemoved &fSpectator Location &bfrom map &f" + mapData.getMapName());
                        return;
                    }

                    Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());
                    mapData.setSpectateLocation(new MapLocation(blockLocation.getX(), blockLocation.getY() + 1.0, blockLocation.getZ()));
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fSpectator Location &bfor map &f" + mapData.getMapName());

                    return;
                }
                if (item.getDurability() == 3) {
                    if (block == null) {
                        if (mapData.getSpawnLocations().isEmpty()) {
                            profile.sendMessage("&8[&3Xime&8] &cThere are no spawns to remove.");
                            return;
                        }

                        mapData.getSpawnLocations().remove(mapData.getSpawnLocations().size() - 1);
                        profile.sendMessage("&8[&3Xime&8] &cRemoved &fSpawn #" + (mapData.getSpawnLocations().size() + 1) + " &cfor map &f" + mapData.getMapName());
                        return;
                    }

                    Material type = block.getType();
                    double offset = 1.0;
                    Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());

                    if (type == Material.STONE_SLAB2 || type == Material.STEP || type == Material.WOOD_STEP)
                        offset = 0.5;

                    if (!type.isSolid() || type == Material.GOLD_PLATE || type == Material.IRON_PLATE || type == Material.STONE_PLATE || type == Material.WOOD_PLATE)
                        offset = 0.0;

                    mapData.getSpawnLocations().add(new MapLocation(blockLocation.getX(), blockLocation.getY() + offset, blockLocation.getZ()));
                    profile.sendMessage("&8[&3Xime&8] &bAdded &fSpawn #" + mapData.getSpawnLocations().size() + " &bfor map &f" + mapData.getMapName());
                    return;
                }

                if (item.getDurability() == 2) {
                    if (block == null) {
                        if (mapData.getDmLocations().isEmpty()) {
                            profile.sendMessage("&8[&3Xime&8] &cThere are no deathmatch spawns to remove.");
                            return;
                        }

                        mapData.getDmLocations().remove(mapData.getDmLocations().size() - 1);
                        profile.sendMessage("&8[&3Xime&8] &cRemoved &fDeathmatch Spawn #" + (mapData.getDmLocations().size() + 1) + " &cfor map &f" + mapData.getMapName());
                        return;
                    }

                    Material type = block.getType();
                    double offset = 1.0;
                    Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());

                    if (type == Material.STONE_SLAB2 || type == Material.STEP || type == Material.WOOD_STEP)
                        offset = 0.5;

                    if (!type.isSolid() || type == Material.GOLD_PLATE || type == Material.IRON_PLATE || type == Material.STONE_PLATE || type == Material.WOOD_PLATE)
                        offset = 0.0;

                    mapData.getDmLocations().add(new MapLocation(blockLocation.getX(), blockLocation.getY() + offset, blockLocation.getZ()));
                    profile.sendMessage("&8[&3Xime&8] &bAdded &fDeathmatch Spawn #" + mapData.getDmLocations().size() + " &bfor map &f" + mapData.getMapName());
                    return;
                }
            }
        }
    }

    @EventHandler
    private void onBuildChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof BuildServerable) {
            BuildServerable serverable = (BuildServerable) event.getServerable();
            MapData mapData = serverable.getMapData();

            if (serverable.getEditor() != profile) return;

            switch (serverable.getInputType()) {
                case NAME:
                    mapData.setMapName(event.getMessage());
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fMap Name &bfor " + mapData.getMapName());
                    break;
                case AUTHOR:
                    mapData.setMapAuthor(event.getMessage());
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fMap Author(s) &bfor " + mapData.getMapName() + " &bto &f" + mapData.getMapAuthor());
                    break;
                case LINK:
                    mapData.setMapLink(event.getMessage());
                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fMap Link &bfor " + mapData.getMapName() + " &bto &f" + mapData.getMapLink());
                    break;
            }
            serverable.setInputType(InputType.NONE);
        }
    }

}
