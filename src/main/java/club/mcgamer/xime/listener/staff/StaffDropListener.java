package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class StaffDropListener extends IListener {

    @EventHandler
    private void onStaffDrop(ServerDropItemEvent event) {
        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onStaffPickup(ServerPickupItemEvent event) {
        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
