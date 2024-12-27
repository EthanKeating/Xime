package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerQuitListener extends IListener {

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        ProfileHandler profileHandler = plugin.getProfileHandler();
        Profile profile = profileHandler.getProfile(player);

        profile.getServerable().remove(profile);
        profileHandler.removeProfile(player);
    }


}
