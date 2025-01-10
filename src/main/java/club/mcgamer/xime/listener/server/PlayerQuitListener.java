package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerQuitListener extends IListener {

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Player player = event.getPlayer();
        ProfileHandler profileHandler = plugin.getProfileHandler();
        DataHandler dataHandler = plugin.getDataHandler();
        Profile profile = profileHandler.getProfile(player);

        if (profile != null && profile.getServerable() != null) {
            profile.getServerable().remove(profile);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                dataHandler.updatePlayerData(profile.getPlayerData());
            });
        }
        profileHandler.removeProfile(player);

        plugin.getDisguiseHandler().getDisguises().remove(player.getUniqueId());
    }


}
