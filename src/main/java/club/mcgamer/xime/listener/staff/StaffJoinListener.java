package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class StaffJoinListener extends IListener {

    @EventHandler
    private void onStaffJoin(ServerJoinEvent event) {
        if (event.getServerable() instanceof StaffServerable) {

            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
                loopPlayer.hidePlayer(player);
                player.showPlayer(loopPlayer);
            });

            profile.sendMessage("&8[&3Xime&8] &aYou have been added to staff mode&8.");

            PlayerUtil.refresh(profile);
            player.teleport(player.getLocation().add(0, 0.01, 0));
            player.setAllowFlight(true);
            player.setFlying(true);
            PlayerUtil.setFlying(profile);
        }
    }

}
