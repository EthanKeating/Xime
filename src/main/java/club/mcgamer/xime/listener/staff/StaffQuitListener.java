package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.server.event.ServerQuitEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class StaffQuitListener extends IListener {

    @EventHandler
    private void onStaffQuit(ServerQuitEvent event) {
        if (event.getServerable() instanceof StaffServerable) {


            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
                loopPlayer.hidePlayer(player);
                player.hidePlayer(loopPlayer);
            });

            profile.sendMessage("&8[&3Xime&8] &cYou have been removed from staff mode&8.");
        }
    }

}
