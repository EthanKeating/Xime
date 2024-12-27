package club.mcgamer.xime.world;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.world.generator.EmptyChunkGenerator;
import club.mcgamer.xime.world.impl.WorldQueue;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Slime;
import org.bukkit.event.world.WorldInitEvent;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class WorldHandler {
    private final XimePlugin plugin;
    private final SlimePlugin slime;

    @Getter private final WorldQueue worldQueue;

    public WorldHandler(XimePlugin plugin) {
        this.plugin = plugin;
        this.slime = plugin.getSlimePlugin();
        this.worldQueue = new WorldQueue(plugin);

        Bukkit.getScheduler().runTaskTimer(plugin, worldQueue::process, 1, 1);
    }


    @SneakyThrows
    public void convert(String bukkitWorldName, String saveWorldName) {
        SlimeLoader loader = slime.getLoader("file");

        File worldDir = Paths.get(Bukkit.getWorldContainer().toString(), bukkitWorldName).toFile();
        File oldSlime = new File(new File(".").getAbsolutePath() + "/slime_worlds/" + saveWorldName + ".slime"); //Hub

        if (oldSlime.exists() && !oldSlime.delete()) {
            File newSlime = new File(new File(".").getAbsolutePath() + "/slime_worlds/" + saveWorldName + "_new" + ".slime");
            if (newSlime.exists())
                newSlime.delete();
            slime.importWorld(worldDir, saveWorldName + "_new", loader);
        } else {
            slime.importWorld(worldDir, saveWorldName, loader);
        }
    }

    //Load world from worlds/<worldName>
    public World loadBukkit(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generatorSettings("2;0;1");
        worldCreator.generator(new EmptyChunkGenerator());
        worldCreator.generateStructures(false);

        return Bukkit.createWorld(worldCreator);
    }

    public void loadSlime(String worldName, String slimeTemplate) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                SlimeLoader loader = slime.getLoader("file");

                SlimePropertyMap propertyMap = new SlimePropertyMap();

                propertyMap.setString(SlimeProperties.DIFFICULTY, "normal");
                propertyMap.setString(SlimeProperties.WORLD_TYPE, "flat");

                propertyMap.setInt(SlimeProperties.SPAWN_X, 0);
                propertyMap.setInt(SlimeProperties.SPAWN_Y, 64);
                propertyMap.setInt(SlimeProperties.SPAWN_Z, 0);

                propertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
                propertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
                propertyMap.setBoolean(SlimeProperties.PVP, true);

                SlimeWorld slimeWorld = slime.loadWorld(loader, slimeTemplate, true, propertyMap).clone(worldName);
                worldQueue.add(slimeWorld);
//                Bukkit.getScheduler().runTask(plugin, () -> {
//                    slime.generateWorld(slimeWorld);
//                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void unload(World world, boolean save) {
        Location teleportLocation = Bukkit.getWorlds().get(0).getSpawnLocation();

        ProfileHandler profileHandler = plugin.getProfileHandler();
        ServerHandler serverHandler = plugin.getServerHandler();

        world.getPlayers().forEach(player -> {
            Profile profile = profileHandler.getProfile(player);
            player.teleport(teleportLocation);
            if (profile.getServerable() != null)
                profile.getServerable().remove(profile);

            serverHandler.getFallback().add(profile);
        });
        if (save) world.save();
        Bukkit.unloadWorld(world, save);
    }

}
