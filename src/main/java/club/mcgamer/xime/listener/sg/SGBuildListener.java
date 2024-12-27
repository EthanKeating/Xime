package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerAirInteractEvent;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGBuildListener extends IListener {

    @EventHandler
    private void onSGBreakBlock(ServerBreakBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable)event.getServerable();

            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                return;
            }

            switch (serverable.getGameState()) {
                case LIVEGAME:
                case DEATHMATCH:
                    Material type = event.getBlock().getType();

                    switch (type) {
                        case LEAVES:
                        case LEAVES_2:
                        case CAKE_BLOCK:
                        case WEB:
                        case FIRE:
                        case LONG_GRASS:
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:
                        case VINE:
                        case WATER_LILY:
                        case RED_ROSE:
                            return;
                    }
                    event.getEvent().setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onSGPlaceBlock(ServerPlaceBlockEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable)event.getServerable();

            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                return;
            }

            switch (serverable.getGameState()) {
                case LIVEGAME:
                case DEATHMATCH:
                    Material type = event.getBlock().getType();

                    switch (type) {
                        case DOUBLE_PLANT:
                        case YELLOW_FLOWER:
                        case VINE:
                        case WATER_LILY:
                        case RED_ROSE:
                        case TNT:
                        case WEB:
                        case CAKE_BLOCK:
                        case FIRE:
                        case DEAD_BUSH:
                            return;
                    }
                    event.getEvent().setCancelled(true);
            }
        }
    }
}
