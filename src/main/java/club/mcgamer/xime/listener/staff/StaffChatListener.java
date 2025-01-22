package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class StaffChatListener extends IListener {

    @EventHandler
    private void onStaffChat(ServerChatEvent event) {
        //[Hub 1] %chat message% - Allow staff mode users to see every chat message on every server

        if (!(event.getServerable() instanceof StaffServerable serverable)) {
            Profile profile = event.getProfile();
            plugin.getProfileHandler().getProfiles().stream().filter(loopProfile -> loopProfile.getServerable() instanceof StaffServerable).forEach(loopProfile -> {
                TextUtil.sendStaffMessage(loopProfile, profile,
                        TextUtil.translate("&7[" + event.getServerable() + "] " + event.getProfile().getDisplayNameBypassDisguise() + "&7: ") + event.getMessage());
            });
            return;
        }

        Profile profile = event.getProfile();

        Bukkit.getScheduler().runTask(plugin, () -> profile.getPlayer().performCommand("staffchat " + event.getMessage()));
    }

}
