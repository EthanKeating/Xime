package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class HubDamageListener extends IListener {

    @EventHandler
    private void onHubDamage(ServerDamageEvent event) {
        if (event.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) event.getServerable();
            event.getEvent().setCancelled(true);

            if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.VOID) {
                event.getVictim().getPlayer().teleport(serverable.getSpawnLocation());
            }

        }
    }

}
