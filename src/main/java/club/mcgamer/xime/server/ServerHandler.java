package club.mcgamer.xime.server;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.server.task.AutoBroadcastTask;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServerHandler {

    private final XimePlugin plugin;
    @Getter private final List<Serverable> serverList = new ArrayList<>();

    @Getter @Setter private boolean isWhitelisted = false;

    public ServerHandler(XimePlugin plugin) {
        this.plugin = plugin;

        String hubWorldName = HubServerable.MAP_NAME + "-1";

        new AutoBroadcastTask(plugin);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getWorldHandler().loadSlime(hubWorldName, HubServerable.MAP_NAME);
        }, 10);


        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getWorldHandler().loadSlime(SGServerable.LOBBY_NAME + "-" + 1, SGServerable.LOBBY_NAME + 1);
        }, 20);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getWorldHandler().loadSlime(SGServerable.LOBBY_NAME + "-" + 2, SGServerable.LOBBY_NAME + 2);
        }, 30);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getWorldHandler().loadSlime(SGServerable.LOBBY_NAME + "-" + 3, SGServerable.LOBBY_NAME + 3);
        }, 40);


        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (getByClass(StaffServerable.class).isEmpty())
                new StaffServerable();

            if (getServerList().stream().filter(serverable -> serverable instanceof HubServerable).count() < 5)
                new HubServerable();
            else if (getServerList().stream().filter(serverable -> !(serverable instanceof SGMakerServerable))
                    .filter(serverable -> serverable instanceof SGServerable).count() < 36)
                    new SGServerable();
            else if (getServerList().stream().filter(serverable -> serverable instanceof BGServerable).count() < 4)
                new BGServerable();
        }, 60, 1);

        Bukkit.getScheduler().runTaskLater(plugin, BGServerable::new, 60L);
    }

    public List<Serverable> getByClass(Class<?> clazz) {
        return serverList.stream().filter(serverable -> serverable.getClass().equals(clazz))
                .collect(Collectors.toList());
    }

    public Serverable getFallback() {
        Optional<Serverable> optionalFallback = serverList
                .stream()
                .filter(serverable -> serverable instanceof HubServerable)
                .filter(serverable -> !serverable.isFull())
                .filter(serverable -> serverable.getPlayerList().size() / 2 < serverable.getMaxPlayers())
                .max(Comparator.comparingInt(serverable -> serverable.getPlayerList().size()));

        if (optionalFallback.isEmpty())
            optionalFallback = serverList.stream().filter(serverable -> serverable instanceof HubServerable)
                    .filter(serverable -> !serverable.isFull()).findAny();


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
