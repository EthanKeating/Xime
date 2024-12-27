package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerAirInteractEvent;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteractWrapper extends IListener {

    @EventHandler
    private void onItemInteract(PlayerInteractEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null || event.getItem() == null || !event.getItem().hasItemMeta()
                || event.getAction() == Action.PHYSICAL
                || event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

        Bukkit.getPluginManager().callEvent(new ServerItemInteractEvent(
                profile,
                profile.getServerable(),
                event.getItem(),
                event));
    }

    @EventHandler
    private void onAirInteract(PlayerInteractEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null || event.getItem() != null
                || event.getAction() == Action.PHYSICAL
                || event.getAction() == Action.LEFT_CLICK_AIR
                || event.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

        Bukkit.getPluginManager().callEvent(new ServerAirInteractEvent(
                profile,
                profile.getServerable(),
                event));
    }

}
