package club.mcgamer.xime.listener.sgmaker;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class SGMakerLoadListener extends IListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onBuildLoad(ServerLoadEvent event) {
        if (event.getServerable() instanceof SGMakerServerable) {
            SGMakerServerable serverable = (SGMakerServerable) event.getServerable();

            Profile owner = serverable.getOwner();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {

                if (owner == null || owner.getPlayer() == null || !owner.getPlayer().isOnline()) {
                    serverable.close();
                    return;
                }

                if (serverable.getGameState() == GameState.LOBBY && !serverable.getPlayerList().contains(owner)) {
                    serverable.add(owner);
                }
            }, 5L);
        }
    }

}
