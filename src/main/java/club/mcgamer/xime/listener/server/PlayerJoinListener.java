package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener extends IListener {

    @EventHandler
    private void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        ProfileHandler profileHandler = plugin.getProfileHandler();
        ServerHandler serverHandler = plugin.getServerHandler();

        if (Bukkit.getPlayer(event.getUniqueId()) != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextUtil.translate("&cYou are already connected to this server, please rejoin"));
            Bukkit.getPlayer(event.getUniqueId()).kickPlayer(ChatColor.RED + "Logged in from another location.");
        }

        Profile profile = profileHandler.createProfile(event.getUniqueId());

    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        ProfileHandler profileHandler = plugin.getProfileHandler();
        ServerHandler serverHandler = plugin.getServerHandler();

        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfile(player);

        profile.complete();

        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.hidePlayer(player);
            player.hidePlayer(loopPlayer);
        });

        serverHandler.getFallback().add(profile);

        if (profile.getServerable() == null) {
            player.kickPlayer(TextUtil.translate("&cCould not locate a server for you!"));
        }

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = new WrapperPlayServerPlayerListHeaderAndFooter(
                Component.text(TextUtil.translate("&aYou are playing on the &6MCGamer Network&a! &eeu.mcgamer.club")),
                Component.text(TextUtil.translate("&aVisit our store at &eshop.mcgamer.club&a!")));

        profile.getUser().sendPacket(headerAndFooter);
        profile.getUser().flushPackets();

        if(!player.hasPermission("xime.gold"))
            profile.getPlayerData().setSilentJoin(false);
    }


}
