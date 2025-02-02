package club.mcgamer.xime.server.task;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AutoBroadcastTask extends BukkitRunnable {

    private final XimePlugin plugin;
    private final int repeatDurationSeconds;

    private final List<String> broadcastMessages;
    private int broadcastIndex = 0;

    public AutoBroadcastTask(XimePlugin plugin) {
        this.plugin = plugin;
        this.repeatDurationSeconds = plugin.getConfig().getInt("broadcast.delay");
        this.broadcastMessages = plugin.getConfig().getStringList("broadcast.messages");

        runTaskTimer(plugin, repeatDurationSeconds * 20L, repeatDurationSeconds * 20L);
    }

    @Override
    public void run() {
        if (broadcastMessages.isEmpty()) return;

        if (broadcastIndex >= broadcastMessages.size())
            broadcastIndex = 0;

        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(TextUtil.translate("&8[&eMCGamer&8] &f" + broadcastMessages.get(broadcastIndex))));
    }
}
