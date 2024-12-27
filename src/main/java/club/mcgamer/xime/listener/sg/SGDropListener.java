package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGDropListener extends IListener {

    @EventHandler
    private void onSGDrop(ServerPickupItemEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            if (!serverable.getSpectatorList().contains(profile)) {
                switch (serverable.getGameState()) {
                    case LIVEGAME:
                    case PREDEATHMATCH:
                    case DEATHMATCH:
                        return;
                }
            }
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onSGInteract(ServerPlaceBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            if (!serverable.getSpectatorList().contains(profile)) {
                switch (serverable.getGameState()) {
                    case LIVEGAME:
                    case PREDEATHMATCH:
                    case DEATHMATCH:
                        return;
                }
            }
            event.getEvent().setCancelled(true);
        }
    }
}
