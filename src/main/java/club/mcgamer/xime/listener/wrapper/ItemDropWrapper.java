package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemDropWrapper extends IListener {

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null) return;

        Bukkit.getPluginManager().callEvent(new ServerDropItemEvent(
                profile,
                profile.getServerable(),
                event));
    }

    @EventHandler
    private void onItemPickup(PlayerPickupItemEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null) return;

        Bukkit.getPluginManager().callEvent(new ServerPickupItemEvent(
                profile,
                profile.getServerable(),
                event));
    }

}
