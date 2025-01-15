package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.stream.Collectors;

public class StaffInteractListener extends IListener {

    @EventHandler
    private void onStaffInteract(ServerInteractEvent event) {
        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

}
