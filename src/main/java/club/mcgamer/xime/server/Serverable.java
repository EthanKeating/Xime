package club.mcgamer.xime.server;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.server.event.ServerQuitEvent;
import club.mcgamer.xime.server.event.ServerStartEvent;
import club.mcgamer.xime.server.event.ServerStopEvent;
import club.mcgamer.xime.world.WorldHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public abstract class Serverable {

    private final int serverId;

    @Getter(AccessLevel.NONE) private final Set<World> worlds = new HashSet<>();

    private final List<Profile> playerList = new ArrayList<Profile>();
    @Setter private int maxPlayers = 64;

    @Setter private String worldName;
    private World world;
    private long worldStartTime = System.currentTimeMillis();

    @Setter private SidebarAdapter sidebarAdapter = SidebarAdapter.DEFAULT;
    @Setter private BossbarAdapter bossbarAdapter = BossbarAdapter.DEFAULT;

    @Setter private boolean joinable = false;

    protected final XimePlugin plugin;

    public Serverable() {
        this.plugin = XimePlugin.getPlugin(XimePlugin.class);
        serverId = getNextId();

        ServerHandler serverHandler = plugin.getServerHandler();
        serverHandler.getServerList().add(this);

        Bukkit.getPluginManager().callEvent(new ServerStartEvent(this));
    }

    public abstract TemporaryData createTemporaryData();

    public void stop() {
        ServerHandler serverHandler = plugin.getServerHandler();
        WorldHandler worldHandler = plugin.getWorldHandler();

        worlds.forEach(loopWorld -> worldHandler.unload(loopWorld, false));
        serverHandler.getServerList().remove(this);
        Bukkit.getPluginManager().callEvent(new ServerStopEvent(this));
    }

    public void add(Profile profile) {
        Player player = profile.getPlayer();

        if (!isJoinable()) {
            profile.sendMessage("&cThis server is not currently joinable");
            return;
        }

        if (profile.getServerable() != null)
            profile.getServerable().remove(profile);

        getPlayerList().stream().map(Profile::getPlayer).forEach(loopPlayer -> {
            loopPlayer.showPlayer(player);
            player.showPlayer(loopPlayer);
        });

        playerList.add(profile);
        profile.setServerable(this);
        profile.setTemporaryData(createTemporaryData());
        Bukkit.getPluginManager().callEvent(new ServerJoinEvent(profile, profile.getServerable()));

    }

    public void remove(Profile profile) {
        Player player = profile.getPlayer();
        Serverable serverable = profile.getServerable();

        serverable.getPlayerList().stream().map(Profile::getPlayer).forEach(loopPlayer -> {
            loopPlayer.hidePlayer(player);
            player.hidePlayer(loopPlayer);
        });

        playerList.remove(profile);
        Bukkit.getPluginManager().callEvent(new ServerQuitEvent(profile, serverable));

    }

    public void setWorld(String worldName, String worldTemplate) {
        WorldHandler worldHandler = plugin.getWorldHandler();

        worldStartTime = System.currentTimeMillis();
        this.worldName = worldName;
        worldHandler.loadSlime(worldName, worldTemplate);
        setJoinable(false);
    }

    public void setWorld(String bukkitWorldName) {
        WorldHandler worldHandler = plugin.getWorldHandler();

        worldStartTime = System.currentTimeMillis();
        this.worldName = bukkitWorldName;
        worldHandler.loadBukkit(bukkitWorldName);
    }

    public void overrideWorld(World world) {
        this.world = world;
        worlds.add(world);
    }

    public boolean isWorldNull() {
        return !this.worldName.equals(world.getName());
    }

    @Override
    public String toString() {
        return this.getClass()
                .getSimpleName()
                .replace("Serverable", "") + "-" + serverId;
    }

    protected int getNextId() {
        List<Serverable> filteredServerList = plugin.getServerHandler().getServerList().stream()
                .filter(server -> server.toString().split("-")[0].equalsIgnoreCase(toString().split("-")[0]))
                .collect(Collectors.toList());

        int maxId = filteredServerList.stream()
                .mapToInt(Serverable::getServerId)
                .max()
                .orElse(0);

        return IntStream.range(1, maxId + 2)
                .filter(i -> filteredServerList.stream().noneMatch(s -> s.getServerId() == i))
                .findFirst()
                .orElse(1);
    }

}
