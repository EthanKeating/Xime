package club.mcgamer.xime.server;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerHandler {

    private final XimePlugin plugin;
    @Getter private final List<Serverable> serverList = new ArrayList<>();

    public ServerHandler(XimePlugin plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (getServerList().stream().filter(serverable -> serverable instanceof HubServerable).count() < 3)
                new HubServerable();
            else if (getServerList().stream().filter(serverable -> serverable instanceof SGServerable).count() < 36)
                new SGServerable();
        }, 20, 20);
    }

    public Serverable getFallback() {
        Optional<Serverable> optionalFallback = serverList
                .stream()
                .filter(serverable -> serverable instanceof HubServerable)
                .filter(serverable -> (double) serverable.getPlayerList().size() / serverable.getMaxPlayers() < 0.5)
                .findFirst();
        return optionalFallback.orElse(null);
    }

    public Serverable getByName(String serverName) {
        return serverList
                .stream()
                .filter(serverable -> serverable.toString().equalsIgnoreCase(serverName))
                .findFirst()
                .orElse(null);
    }

    public void announce(String message) {
        String finalMessage = TextUtil.translate("&7[&eServer Handler&7] &f" + message);
//        Bukkit.getOnlinePlayers().stream()
//                .filter(player -> player.hasPermission("admin"))
//                .forEach(player -> player.sendMessage(finalMessage));

        Bukkit.getServer().getConsoleSender().sendMessage(finalMessage);
    }



}
