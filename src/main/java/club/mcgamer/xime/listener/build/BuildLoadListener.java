package club.mcgamer.xime.listener.build;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BuildLoadListener extends IListener {

    @EventHandler
    private void onBuildLoad(ServerLoadEvent event) {
        if (event.getServerable() instanceof BuildServerable) {
            BuildServerable serverable = (BuildServerable) event.getServerable();

            World world = event.getWorld();
            Profile profile = serverable.getEditor();
            Player player = profile.getPlayer();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                serverable.add(profile);
                player.teleport(world.getSpawnLocation());
            }, 5L);
        }
    }

}
