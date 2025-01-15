package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class StaffInteractListener extends IListener {

    @EventHandler
    private void onStaffInteract(ServerInteractEvent event) {
        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
