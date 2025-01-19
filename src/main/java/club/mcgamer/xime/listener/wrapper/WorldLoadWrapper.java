package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.world.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoadWrapper extends IListener {


    @EventHandler
    private void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        ServerHandler serverHandler = plugin.getServerHandler();
        WorldHandler worldHandler = plugin.getWorldHandler();

        if (worldHandler.getWorldQueue().getCurrentWorld() != null && worldHandler.getWorldQueue().getCurrentWorld().equalsIgnoreCase(world.getName()))
            worldHandler.getWorldQueue().remove(world.getName());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            serverHandler.getServerList().stream()
                    .filter(loopServerable -> loopServerable.getWorldName() != null)
                    .filter(loopServerable -> loopServerable.getWorldName().equalsIgnoreCase(world.getName()))
                    .findFirst()
                    .ifPresent(serverable -> {
                        serverHandler.announce("Loaded &6" + world.getName() + "&f for &a" + serverable + "&f in &a" + (System.currentTimeMillis() - serverable.getWorldStartTime()) + "ms");
                        serverable.overrideWorld(world);
                        Bukkit.getPluginManager().callEvent(new ServerLoadEvent(serverable, world));
                        serverable.setJoinable(true);
                    });
        }, 2L);
    }

}
