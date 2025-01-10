package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatWrapper extends IListener {

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfile(player);
        Serverable serverable = profile.getServerable();

        Bukkit.getPluginManager().callEvent(new ServerChatEvent(profile, serverable, event.getMessage()));
    }

}
