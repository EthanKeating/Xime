package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerChatEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.w3c.dom.Text;

public class SGChatListener extends IListener {

    @EventHandler
    private void onSGChat(ServerChatEvent event) {
        Profile profile = event.getProfile();

        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            GameState gameState = serverable.getGameState();
            String chatFormat;

            PlayerData playerData = profile.getMockOrRealPlayerData();
            SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();

            if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
                if (event.getMessage().startsWith("@")) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        profile.getPlayer().performCommand("teamchat " + event.getMessage().replaceFirst("@", ""));
                    });
                    return;
                }
            }

            if (gameState == GameState.LOBBY || gameState == GameState.LOADING) {
                chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&f%s&8: &f%s",
                        playerData.getSgPoints(),
                        "%player%",
                        profile.getChatColor()));

                if (serverable instanceof SGMakerServerable makerServerable) {
                    chatFormat = TextUtil.translate(String.format("&f%s&8: &f%s",
                            "%player%",
                            profile.getChatColor()));

                    if (makerServerable.getOwner() == profile)
                        chatFormat = ChatColor.GOLD + "[Host] " + chatFormat;
                } else {
                    chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&f%s&8: &f%s",
                            playerData.getSgPoints(),
                            "%player%",
                            profile.getChatColor()));
                }

                String finalChatFormat = chatFormat;
                serverable.getPlayerList().forEach(loopProfile -> {
                    TextUtil.sendStaffMessage(loopProfile, profile, finalChatFormat + event.getMessage());
                });
                return;
            }

            if (serverable.getTributeList().contains(profile)) {
                SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

                chatFormat = TextUtil.translate(String.format("&8[&a%s&8]&c%s&8|&f%s&8: &f%s",
                        temporaryData.getBounty(),
                        temporaryData.getDistrictId(),
                        "%player%",
                        profile.getChatColor()));

                String finalChatFormat1 = chatFormat;
                serverable.getPlayerList().forEach(loopProfile -> {
                    TextUtil.sendStaffMessage(loopProfile, profile, finalChatFormat1 + event.getMessage());
                });
                return;
            }

            chatFormat = TextUtil.translate(String.format("&8[&e%s&8]&4SPEC&8|&f%s&8: &f%s",
                    playerData.getSgPoints(),
                    "%player%",
                    profile.getChatColor()));

            //Send to everyone
            if (gameState == GameState.ENDGAME || gameState == GameState.CLEANUP || gameState == GameState.RESTARTING) {
                String finalChatFormat2 = chatFormat;
                serverable.getPlayerList().forEach(loopProfile -> {
                    TextUtil.sendStaffMessage(loopProfile, profile, finalChatFormat2 + event.getMessage());
                });
                return;
            }

            //Sent to just spectators
            String finalChatFormat3 = chatFormat;
            serverable.getSpectatorList().forEach(loopProfile ->
                    TextUtil.sendStaffMessage(loopProfile, profile, finalChatFormat3 + event.getMessage()));

        }
    }
}
