package club.mcgamer.xime.listener.build;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.build.input.InputType;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import com.github.retrooper.packetevents.util.Vector3i;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
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

            event.getEvent().setCancelled(true);

            if (event.getEvent().getClickedBlock() != null) {
                Block block = event.getEvent().getClickedBlock();
                Vector3i blockLocation = new Vector3i(block.getX(), block.getY(), block.getZ());

                if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 0) {
                    mapData.setCenterLocation(new MapLocation(blockLocation.getX(), blockLocation.getY() + 1.0, blockLocation.getZ()));
                    world.setSpawnLocation(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());

                    profile.sendMessage("&8[&3Xime&8] &bUpdated &fMap Center &bfor map &f" + mapData.getMapName());
                    return;
                }

                if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 3) {
                    double offset = world.getBlockAt(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ()).getType().isSolid() ? 1.0 : 0.0;

                    mapData.getSpawnLocations().add(new MapLocation(blockLocation.getX(), blockLocation.getY() + offset, blockLocation.getZ()));

                    profile.sendMessage("&8[&3Xime&8] &bAdded &fSpawn #" + mapData.getSpawnLocations().size() + " &bfor map &f" + mapData.getMapName());
                    return;
                }
            } else {
                if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 0) {
                    mapData.setCenterLocation(new MapLocation(0, 64, 0));
                    world.setSpawnLocation(0, 64, 0);

                    profile.sendMessage("&8[&3Xime&8] &cRemoved &fMap Center &bfrom map &f" + mapData.getMapName());
                    return;
                }

                if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 3 && !mapData.getSpawnLocations().isEmpty()) {
                    mapData.getSpawnLocations().remove(mapData.getSpawnLocations().size() - 1);

                    profile.sendMessage("&8[&3Xime&8] &cRemoved &fSpawn #" + (mapData.getSpawnLocations().size() + 1) + " &cfor map &f" + mapData.getMapName());
                    return;
                }
            }
            if (item.getType() == Material.CHEST) {
                serverable.updateChests();
            }
            if (item.getType() == Material.GLASS) {
                serverable.optimize();
            }

            if (item.getType() == Material.ANVIL) {
                serverable.setInputType(InputType.NAME);

                profile.sendMessage("&a")
                        .sendMessage("&a&lPlease enter the map name:")
                        .sendMessage("&a");

                return;
            }

            if (item.getType() == Material.NAME_TAG) {
                serverable.setInputType(InputType.AUTHOR);

                profile.sendMessage("&a")
                        .sendMessage("&a&lPlease enter the map author:")
                        .sendMessage("&a");

                return;
            }

            if (item.getType() == Material.EYE_OF_ENDER) {
                serverable.setInputType(InputType.LINK);

                profile.sendMessage("&a")
                        .sendMessage("&a&lPlease enter the map link:")
                        .sendMessage("&a");

                return;
            }

            if (item.getType() == Material.STAINED_CLAY && item.getDurability() == 6) {
                profile.sendMessage("&8[&3Xime&8] &aDiscarded changes for &f" + mapData.getMapName());
                serverable.discard();
                serverable.stop();
                return;
            }

            if (item.getType() == Material.STAINED_CLAY && item.getDurability() == 5) {
                //save
                MapData.save(serverable.getWorldName(), mapData);
                serverable.save();
                serverable.stop();
                profile.sendMessage("&8[&3Xime&8] &aSaved changes for &f" + mapData.getMapName());
                return;
            }
            event.getEvent().setCancelled(false);

        }
    }

    @EventHandler
    private void onBuildChat(PlayerChatEvent event) {
        ProfileHandler profileHandler = plugin.getProfileHandler();
        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfile(player);

        if (profile.getServerable() instanceof BuildServerable) {
            BuildServerable serverable = (BuildServerable) profile.getServerable();
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
        }
    }

}
