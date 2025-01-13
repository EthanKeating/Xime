package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.event.EventHandler;

public class SGChatListener extends IListener {

    @EventHandler
    private void onSGChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            GameState gameState = serverable.getGameState();
            String chatFormat;

            PlayerData playerData = profile.getMockOrRealPlayerData();

            if (gameState == GameState.LOBBY) {
                chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&f%s&8: &f%s",
                        playerData.getSgPoints(),
                        profile.getDisplayName(),
                        profile.getChatColor()));

                serverable.announceRawUncoloured(chatFormat + event.getMessage());
                return;
            }

            if (serverable.getTributeList().contains(profile)) {
                SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

                chatFormat = TextUtil.translate(String.format("&8[&a%s&8]&c%s&8|&f%s&8: &f%s",
                        temporaryData.getBounty(),
                        temporaryData.getDistrictId(),
                        profile.getDisplayName(),
                        profile.getChatColor()));

                serverable.announceRawUncoloured(chatFormat + event.getMessage());
                return;
            }

            chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&4SPEC&8|&f%s&8: &f%s",
                    playerData.getSgPoints(),
                    profile.getDisplayName(),
                    profile.getChatColor()));

            //Send to everyone
            if (gameState == GameState.ENDGAME || gameState == GameState.CLEANUP || gameState == GameState.RESTARTING) {
                serverable.announceRawUncoloured(chatFormat + event.getMessage());
                return;
            }

            //Sent to just spectators
            serverable.getSpectatorList().stream().map(Profile::getPlayer).forEach(player ->
                    player.sendMessage(chatFormat + event.getMessage()));

        }
    }
}
