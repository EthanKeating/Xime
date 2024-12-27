package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HubChatListener extends IListener {

    private boolean globalChat = true;

    @EventHandler
    private void onHubChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) event.getServerable();

            Set<Profile> hubProfiles = new HashSet<>(serverable.getPlayerList());

            String displayName = profile.getDisplayName();
            String chatColor = profile.getChatColor();

            if (globalChat) {
                plugin.getServerHandler().getServerList().stream()
                        .filter(loopServerable -> loopServerable instanceof HubServerable)
                        .forEach(loopServerable -> hubProfiles.addAll(loopServerable.getPlayerList()));
            }

            hubProfiles.stream().map(Profile::getPlayer).forEach(loopProfile -> loopProfile.sendMessage(
                    TextUtil.translate(String.format("%s&8: &f%s", displayName, chatColor)) + event.getMessage()
            ));

        }
    }
}
