package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StaffInteractListener extends IListener {

    @EventHandler
    private void onStaffInteract(ServerInteractEvent event) {
        if (event.getServerable() instanceof StaffServerable) {
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onStaffInteract(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile == null) return;

        Serverable serverable = profile.getServerable();

        if (serverable instanceof StaffServerable) {
            event.setCancelled(true);
        }
    }

}
