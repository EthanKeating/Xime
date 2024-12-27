package club.mcgamer.xime.design;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.design.bossbar.BossbarImpl;
import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.design.sidebar.SidebarImpl;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DesignThread extends Thread {

    private final XimePlugin plugin;
    private final ServerHandler serverHandler;
    private final ProfileHandler profileHandler;

    private WeakHashMap<UUID, Serverable> previousServerables = new WeakHashMap<>();
    private int serverTickId;

    public DesignThread(XimePlugin plugin) {
        super("xime-design-thread");
        this.plugin = plugin;
        this.serverHandler = plugin.getServerHandler();
        this.profileHandler = plugin.getProfileHandler();
    }

    private void update() {
        serverTickId++;

        for(UUID uuid : new WeakHashMap<>(previousServerables).keySet()) {
            if (uuid == null || Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline())
                previousServerables.remove(uuid);
        }

        new CopyOnWriteArrayList<>(serverHandler.getServerList()).forEach(serverable -> {
            if (serverable != null) {
                serverable.getSidebarAdapter().tick();
                serverable.getBossbarAdapter().tick();
            }
        });

        new CopyOnWriteArrayList<>(profileHandler.getProfiles()).forEach(profile -> {
            Serverable serverable = profile.getServerable();
            Serverable previousServerable = previousServerables.getOrDefault(profile.getUuid(), null);

            if (serverable != null && profile.getPlayer() != null) {

                SidebarImpl sidebarImpl = profile.getSidebarImpl();
                SidebarAdapter sidebarAdapter = serverable.getSidebarAdapter();
                if (serverable != previousServerable || (sidebarImpl != null && serverTickId % sidebarAdapter.getUpdateRate() == 0))
                    sidebarImpl.tick(sidebarAdapter);

                BossbarImpl bossbarImpl = profile.getBossbarImpl();
                BossbarAdapter bossbarAdapter = serverable.getBossbarAdapter();
                if (bossbarImpl != null)
                    bossbarImpl.tick(bossbarAdapter);
            }
        });
    }

    @SneakyThrows
    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            update();
            Thread.sleep(Math.max(0, 50 - (System.currentTimeMillis() - startTime)));
        }
    }
}
