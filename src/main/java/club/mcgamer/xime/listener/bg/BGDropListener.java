package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class BGDropListener extends IListener {

    @EventHandler
    private void onBGDropItem(ServerDropItemEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onBGPickupItem(ServerPickupItemEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();
            event.getEvent().setCancelled(true);
            event.getEvent().getItem().remove();
        }
    }
}
