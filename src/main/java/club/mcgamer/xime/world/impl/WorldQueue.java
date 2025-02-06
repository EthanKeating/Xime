package club.mcgamer.xime.world.impl;

import club.mcgamer.xime.XimePlugin;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class WorldQueue {

    private final Queue<String> nameQueue = new LinkedList<>();
    private final ConcurrentHashMap<String, SlimeWorld> nameToWorldMap = new ConcurrentHashMap<>();
    public String currentWorld = null;

    private final XimePlugin plugin;
    private final SlimePlugin slime;

    public WorldQueue(XimePlugin plugin) {
        this.plugin = plugin;
        this.slime = plugin.getSlimePlugin();

    }

    public void process() {

        if (currentWorld != null)
            return;

        if (nameQueue.isEmpty())
            return;

        currentWorld = nameQueue.poll();
        SlimeWorld world = nameToWorldMap.get(currentWorld);

        slime.generateWorld(world);
    }

    public void add(SlimeWorld slimeWorld) {
        String worldName = slimeWorld.getName();

        nameQueue.add(worldName);
        nameToWorldMap.put(worldName, slimeWorld);
    }

    public void remove(String worldName) {
        nameToWorldMap.remove(worldName);
        currentWorld = null;
    }

}
