package club.mcgamer.xime.server.task;

import club.mcgamer.xime.XimePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AutoBroadcastTask extends BukkitRunnable {

    private final XimePlugin plugin;
    private final int repeatDurationSeconds = 60;

    private final List<String> broadcastMessages;
    private int broadcastIndex = 0;

    public AutoBroadcastTask(XimePlugin plugin) {
        this.plugin = plugin;
        this.broadcastMessages = plugin.getConfig().getStringList("broadcastMessages");

        runTaskTimer(plugin, repeatDurationSeconds * 20, repeatDurationSeconds * 20);
    }

    @Override
    public void run() {
        if (broadcastMessages.isEmpty()) return;

        if (broadcastIndex >= broadcastMessages.size())
            broadcastIndex = 0;

        plugin.getProfileHandler().getProfiles().forEach(profile -> profile.sendMessage("&8[&eMCGamer&8] &f" + broadcastMessages.get(broadcastIndex)));
    }
}
