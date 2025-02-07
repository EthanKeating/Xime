package club.mcgamer.xime.listener.server;

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
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (Bukkit.getPlayer(event.getUniqueId()) != null)
                    Bukkit.getPlayer(event.getUniqueId()).kickPlayer(ChatColor.RED + "Logged in from another location.");
            });

            return;
        }
        if (plugin.getProfileHandler().getProfile(event.getUniqueId()) != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, TextUtil.translate("&cYou are already connected to this server, please rejoin"));
            if (Bukkit.getPlayer(event.getUniqueId()) == null) {
                profileHandler.removeProfile(event.getUniqueId());
            }
            return;
        }

        Profile profile = profileHandler.createProfile(event.getUniqueId());

    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        if (plugin.getServerHandler() == null || plugin.getServerHandler().getFallback() == null) {
            event.getPlayer().kickPlayer(TextUtil.translate("&cCould not locate a server for you!"));
            return;
        }

        ProfileHandler profileHandler = plugin.getProfileHandler();
        ServerHandler serverHandler = plugin.getServerHandler();

        Player player = event.getPlayer();
        Profile profile = profileHandler.getProfile(player);

        profile.complete();

        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.hidePlayer(player);
            player.hidePlayer(loopPlayer);
        });

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline())
                return;

            Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
                loopPlayer.hidePlayer(player);
                player.hidePlayer(loopPlayer);
            });
        }, 1L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline())
                return;

            Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
                loopPlayer.hidePlayer(player);
                player.hidePlayer(loopPlayer);
            });

            serverHandler.getFallback().add(profile);

            if (profile.getServerable() == null)
                player.kickPlayer(TextUtil.translate("&cCould not locate a server for you!"));
//
//            profile.sendMessage("&8[&eMCGamer&8] &6Change-Log &cv5.1.0")
//                    .sendMessage("   &8- &fEnable Pre Game joining for all players.")
//                    .sendMessage("   &8- &fAdded lobby map shuffling (2 new lobbies!)")
//                    .sendMessage("   &8- &fPatched disguise revealing methods")
//                    .sendMessage("   &8- &fIncreased lobby countdown from 2m to 3m")
//                    .sendMessage("   &8- &fDecreased required lobby players from 6 to 5")
//                    .sendMessage("   &8- &fDecreased arrow stacks in tier 1s from 5 to 2")
//                    .sendMessage("   &8- &fRebalanced arrow crafting items in tier 1s")
//                    .sendMessage("   &8- &fStarted preparation for the `Knockback Trial`");
        }, 2L);

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = new WrapperPlayServerPlayerListHeaderAndFooter(
                Component.text(TextUtil.translate(profile.getLanguage().getTabHeader())),
                Component.text(TextUtil.translate(profile.getLanguage().getTabFooter())));

        profile.getUser().sendPacket(headerAndFooter);
        profile.getUser().flushPackets();

        if(!player.hasPermission("xime.diamond"))
            profile.getPlayerData().setSilentJoin(false);
    }


}
