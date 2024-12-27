package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerHungerLossEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class HubHungerLossListener extends IListener {

    @EventHandler
    private void onHubHungerLoss(ServerHungerLossEvent event) {
        Profile profile = event.getProfile();

        if (profile.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) profile.getServerable();

            event.getEvent().setFoodLevel(20);
        }
    }

}

