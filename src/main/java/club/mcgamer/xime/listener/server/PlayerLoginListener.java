package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerLoginListener extends IListener {

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        String disallowMessage = TextUtil.translate("&8[&eMCGamer&8]" +
                "\n&7The MCGamer Development Server is currently &cWhitelisted" +
                "\n&7For more information, join our Discord&8:" +
                "\n&6https://discord.gg/Twyqpa8tqZ");

        if (player.hasPermission("xime.admin")) {
            Bukkit.getWhitelistedPlayers().add(player);
            Bukkit.reloadWhitelist();
            return;
        }

        if (Bukkit.hasWhitelist())
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, disallowMessage);

    }


}
