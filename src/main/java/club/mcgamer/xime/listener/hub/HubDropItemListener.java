package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class HubDropItemListener extends IListener {

    @EventHandler
    private void onHubDropItem(ServerDropItemEvent event) {
        Profile profile = event.getProfile();

        if (profile.getServerable() instanceof HubServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}

