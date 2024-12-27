package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class HubBreakBlockListener extends IListener {

    @EventHandler
    private void onBlockBreak(ServerBreakBlockEvent event) {
        if (event.getServerable() instanceof HubServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
