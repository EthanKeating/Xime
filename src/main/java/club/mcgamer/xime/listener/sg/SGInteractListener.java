package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerAirInteractEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGInteractListener extends IListener {

    @EventHandler
    private void onSGInteract(ServerAirInteractEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            if (serverable.getSpectatorList().contains(profile))
                switch (serverable.getGameState()) {
                    case LIVEGAME:
                    case PREDEATHMATCH:
                    case DEATHMATCH:
                        player.performCommand("spectate");
                }
        }
    }
}
