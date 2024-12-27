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

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            serverHandler.getServerList().stream()
                    .filter(loopServerable -> loopServerable.getWorldName().equals(world.getName()))
                    .findFirst()
                    .ifPresent(serverable -> {
                        serverHandler.announce("Loaded &6" + world.getName() + "&f for &a" + serverable + "&f in &a" + (System.currentTimeMillis() - serverable.getWorldStartTime()) + "ms");
                        serverable.overrideWorld(world);
                        Bukkit.getPluginManager().callEvent(new ServerLoadEvent(serverable, world));
                        serverable.setJoinable(true);
                        worldHandler.getWorldQueue().remove(world.getName());
                    });
        }, 1L);
    }

}
