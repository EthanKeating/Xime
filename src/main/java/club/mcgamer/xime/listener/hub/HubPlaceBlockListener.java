package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class HubPlaceBlockListener extends IListener {

    @EventHandler
    private void onBlockPlace(ServerPlaceBlockEvent event) {
        if (event.getServerable() instanceof HubServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
