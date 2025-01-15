package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class StaffDamageListener extends IListener {

    @EventHandler
    private void onStaffDamage(ServerDamageEvent event) {
        if (event.getAttacker().isPresent() && event.getAttacker().get().getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
            return;
        }

        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
