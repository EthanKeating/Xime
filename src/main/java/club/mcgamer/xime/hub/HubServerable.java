package club.mcgamer.xime.hub;

import club.mcgamer.xime.hub.data.HubTemporaryData;
import club.mcgamer.xime.hub.design.bossbar.HubBossbarAdapter;
import club.mcgamer.xime.hub.design.sidebar.HubSidebarAdapter;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
public class HubServerable extends Serverable {

    public static final String MAP_NAME = "Hub";

    @Setter private Location spawnLocation;

    public HubServerable() {
        super();

        setSidebarAdapter(new HubSidebarAdapter());
        setBossbarAdapter(new HubBossbarAdapter());

        setWorldName(MAP_NAME + "-1");
        overrideWorld(Bukkit.getWorld(MAP_NAME + "-1"));
        setMaxPlayers(200);
        setMapData(MapData.load(MAP_NAME));
        setJoinable(true);
    }

    public Location getSpawnLocation() {
        Location location = getMapData().getCenterLocation().toBukkit(Bukkit.getWorld(MAP_NAME + "-1"));
        location.setYaw(-90);
        return location;
    }

    public String getPrefix() {
        return "&8[&eSGHQ&8] &f";
    }

    public TemporaryData createTemporaryData() {
        return new HubTemporaryData();
    }

    @Override
    public void add(Profile profile) {
        Player player = profile.getPlayer();

        if (profile.getServerable() == this) {
            profile.sendMessage("&cYou are already connected to that server.");
            return;
        }

        if (getPlayerList().size() >= getMaxPlayers()) {
            profile.sendMessage("&cThat server is full.");

            return;
        }

        if (!isJoinable()) {
            profile.sendMessage("&cThis server is not currently joinable");
            return;
        }

        if (profile.getServerable() != null)
            profile.getServerable().remove(profile);


        plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
            profile.getPlayer().hidePlayer(loopProfile.getPlayer());
            loopProfile.getPlayer().hidePlayer(loopProfile.getPlayer());
        });

        getPlayerList().forEach(loopProfile -> {
            Player loopPlayer = loopProfile.getPlayer();
            HubTemporaryData hubTemporaryData = (HubTemporaryData) loopProfile.getTemporaryData();

            if (!hubTemporaryData.isHidePlayers())
                loopPlayer.showPlayer(player);
            player.showPlayer(loopPlayer);
        });

        getPlayerList().remove(profile);
        getPlayerList().add(profile);
        profile.setServerable(this);
        profile.setTemporaryData(createTemporaryData());
        Bukkit.getPluginManager().callEvent(new ServerJoinEvent(profile, profile.getServerable()));

    }

    public void sendParticle(EnumParticle particle, Player p, float speed, Integer amount) {
        float x = p.getLocation().getBlockX();
        float y = p.getLocation().getBlockY();
        float z = p.getLocation().getBlockZ();

        int range = 7;
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, x, y, z, range, range, range, speed, amount.intValue(), (int[])null);
        (((CraftPlayer)p).getHandle()).playerConnection.sendPacket(packet);
    }

}
