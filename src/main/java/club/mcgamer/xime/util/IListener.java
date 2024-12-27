package club.mcgamer.xime.util;

import club.mcgamer.xime.XimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class IListener implements Listener {

    protected final XimePlugin plugin;

    public IListener() {
        this.plugin = XimePlugin.getPlugin(XimePlugin.class);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
