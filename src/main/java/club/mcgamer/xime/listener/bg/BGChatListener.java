
package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.event.EventHandler;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class BGChatListener extends IListener {

    @EventHandler
    private void onBGChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();

            String chatColor = profile.getChatColor();

            DecimalFormat formatter = new DecimalFormat("#,###");

            int bgPlacement = profile.getPlayerData().getBgGameRank();
            String placementString = "#" + formatter.format(bgPlacement) + " ";
            if (bgPlacement == -1)
                placementString = "";
            else if(bgPlacement <= 10)
                placementString = "&6" + placementString;
            else if(bgPlacement <= 25)
                placementString = "&b" + placementString;
            else if(bgPlacement <= 100)
                placementString = "&e" + placementString;
            else
                placementString = "&7" + placementString;

            String finalPlacementString = placementString;
            serverable.getPlayerList().forEach(loopProfile -> {

                if (loopProfile.getPlayer().hasPermission("xime.staff")) {
                    TextUtil.sendStaffMessage(loopProfile, profile, TextUtil.translate(
                            String.format("%s%s&8: &f%s",
                                    finalPlacementString,
                                    profile.getName().equalsIgnoreCase(profile.getNameBypassDisguise()) ? profile.getDisplayName() : profile.getDisplayName() + "&8(" + profile.getDisplayNameBypassDisguise() + "&8)",
                                    chatColor)) + event.getMessage());
                } else {
                    loopProfile.sendMessage(TextUtil.translate(String.format("%s%s&8: &f%s", finalPlacementString, profile.getDisplayNameBypassDisguise(), chatColor)) + event.getMessage());
                }
                });

        }
    }
}
