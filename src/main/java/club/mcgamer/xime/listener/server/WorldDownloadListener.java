package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class WorldDownloadListener implements PluginMessageListener {

    private final String oldChannel = "wdl|init";

    private final XimePlugin plugin;

    public WorldDownloadListener(XimePlugin plugin) {
        this.plugin = plugin;

        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, oldChannel, this);
    }

    @Override
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] data) {
        if (!channel.equalsIgnoreCase(oldChannel)) return;

        Bukkit.getOnlinePlayers().stream()
                .filter(loopPlayer -> loopPlayer.hasPermission("xime.admin"))
                .forEach(loopPlayer -> loopPlayer.sendMessage(TextUtil.translate("&c" + player.getName() + " has been kicked for attempting to use World Downloader!")));
        player.kickPlayer("&cYou are not allowed to use World Downloader on the SGHQ Network!");
    }
}
