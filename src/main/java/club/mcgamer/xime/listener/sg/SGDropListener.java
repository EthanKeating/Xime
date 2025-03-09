package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SGDropListener extends IListener {

    @EventHandler
    private void onSGPickup(ServerPickupItemEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            if (serverable.getGameState() == GameState.LOBBY
                    || serverable.getGameState() == GameState.LOADING
                    || serverable.getGameState() == GameState.PREGAME)
                event.getEvent().setCancelled(true);

            if (!serverable.getSpectatorList().contains(profile)) {
                Set<Material> blackList = new HashSet<>();

                if(blackList.contains(event.getEvent().getItem().getItemStack().getType())) {
                    event.getEvent().setCancelled(true);
                    event.getEvent().getItem().remove();
                    return;
                }

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
    private void onSGDrop(ServerDropItemEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            if (serverable.getGameState() == GameState.LOBBY || serverable.getGameState() == GameState.LOADING || serverable.getGameState() == GameState.PREGAME) {
                event.getEvent().setCancelled(true);
            }
            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
            }
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
