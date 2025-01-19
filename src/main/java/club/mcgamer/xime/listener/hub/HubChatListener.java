package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class HubChatListener extends IListener {

    private boolean globalChat = true;

    @EventHandler
    private void onHubChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) event.getServerable();

            Set<Profile> hubProfiles = new HashSet<>(serverable.getPlayerList());

            String chatColor = profile.getChatColor();

            if (globalChat) {
                plugin.getServerHandler().getServerList().stream()
                        .filter(loopServerable -> loopServerable instanceof HubServerable)
                        .forEach(loopServerable -> hubProfiles.addAll(loopServerable.getPlayerList()));
            }

            hubProfiles.stream().forEach(loopProfile -> {
                if (loopProfile.getPlayer().hasPermission("xime.staff")) {
                    TextUtil.sendStaffMessage(loopProfile, profile, TextUtil.translate(
                            String.format("%s&8: &f%s",
                                    profile.getName().equalsIgnoreCase(profile.getNameBypassDisguise()) ? profile.getDisplayName() : profile.getDisplayName() + "&8(" + profile.getDisplayNameBypassDisguise() + "&8)",
                                    chatColor)) + event.getMessage());
                } else {
                    loopProfile.sendMessage(TextUtil.translate(String.format("%s&8: &f%s", profile.getDisplayNameBypassDisguise(), chatColor)) + event.getMessage());
                }
                });

        }
    }
}
