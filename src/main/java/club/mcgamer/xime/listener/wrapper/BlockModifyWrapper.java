package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerBreakBlockEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockModifyWrapper extends IListener {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null) return;

        Bukkit.getPluginManager().callEvent(new ServerBreakBlockEvent(
                profile,
                profile.getServerable(),
                event.getBlock(),
                event));
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile(event.getPlayer());

        if (profile.getServerable() == null) return;

        Bukkit.getPluginManager().callEvent(new ServerPlaceBlockEvent(
                profile,
                profile.getServerable(),
                event.getBlock(),
                event));
    }

}
