package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.server.event.ServerPickupItemEvent;
import club.mcgamer.xime.server.event.ServerPlaceBlockEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGChatListener extends IListener {

    @EventHandler
    private void onSGChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            String chatFormat;

            if (!serverable.getSpectatorList().contains(profile)) {
                switch (serverable.getGameState()) {
                    case LOBBY:
                        chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&f%s&8: &f%s",
                                100, //TODO: replace with points once stats are setup
                                profile.getDisplayName(),
                                profile.getChatColor()));

                        serverable.announceRawUncoloured(chatFormat + event.getMessage());
                        break;
                    default:
                        if (serverable.getTributeList().contains(profile)) {
                            SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

                            chatFormat = TextUtil.translate(String.format("&8[&a%s&8]&c%s&8|&f%s&8: &f%s",
                                    temporaryData.getBounty(),
                                    temporaryData.getDistrictId(),
                                    profile.getDisplayName(),
                                    profile.getChatColor()));

                            serverable.announceRawUncoloured(chatFormat + event.getMessage());
                        } else {
                            chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&4SPEC&8|&f%s&8: &f%s",
                                    100, //TODO: replace with points once stats are setup
                                    profile.getDisplayName(),
                                    profile.getChatColor()));

                            serverable.getSpectatorList().stream().map(Profile::getPlayer).forEach(player ->
                                    player.sendMessage(chatFormat + event.getMessage()));
                        }
                }
            }
        }
    }
}
