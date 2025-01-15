package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerLoginListener extends IListener {

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        String disallowMessage = TextUtil.translate("&8[&eMCGamer&8]" +
                "\n&7The MCGamer Development Server is currently &cWhitelisted" +
                "\n&7For more information, join our Discord&8:" +
                "\n&6https://discord.gg/Twyqpa8tqZ");

        if (plugin.getProfileHandler().getProfile(player) != null && plugin.getProfileHandler().getProfile(player).getRank() != null) {
            if (plugin.getProfileHandler().getProfile(player).getRank().getPermissions().contains("xime.staff")) {
                return;
            }
        }
        if (player.hasPermission("xime.staff")) {
            return;
        }

        if (plugin.getServerHandler().isWhitelisted()) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, disallowMessage);
            if (plugin.getProfileHandler().getProfile(player) != null)
                plugin.getProfileHandler().removeProfile(player);
        }

    }


}
