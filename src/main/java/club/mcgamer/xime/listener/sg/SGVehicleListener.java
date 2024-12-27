package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class SGVehicleListener extends IListener {

    @EventHandler
    private void onSGVehicleCollide(VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = plugin.getProfileHandler().getProfile(player);

            if (profile.getServerable() instanceof SGServerable) {
                SGServerable serverable = (SGServerable) profile.getServerable();

                if (serverable.getSpectatorList().contains(profile)) {
                    event.setCancelled(true);
                    event.setCollisionCancelled(true);
                    event.setPickupCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onSGVehicleDamage(VehicleDamageEvent event) {
        if (event.getAttacker() instanceof Player) {
            Player player = (Player) event.getAttacker();
            Profile profile = plugin.getProfileHandler().getProfile(player);

            if (profile.getServerable() instanceof SGServerable) {
                SGServerable serverable = (SGServerable) profile.getServerable();


                if (serverable.getSpectatorList().contains(profile)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onSGVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = (Player) event.getEntered();
            Profile profile = plugin.getProfileHandler().getProfile(player);

            if (profile.getServerable() instanceof SGServerable) {
                SGServerable serverable = (SGServerable) profile.getServerable();

                if (serverable.getSpectatorList().contains(profile)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
