package club.mcgamer.xime.hub;

import club.mcgamer.xime.hub.data.HubTemporaryData;
import club.mcgamer.xime.hub.design.bossbar.HubBossbarAdapter;
import club.mcgamer.xime.hub.design.sidebar.HubSidebarAdapter;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class HubServerable extends Serverable {

    public static final String MAP_NAME = "Hub";

    @Setter private Location spawnLocation;

    public HubServerable() {
        super();

        setSidebarAdapter(new HubSidebarAdapter());
        setBossbarAdapter(new HubBossbarAdapter());

        setWorld(toString(), MAP_NAME);
        setMapData(MapData.load(MAP_NAME));
        snow();
    }

    public TemporaryData createTemporaryData() {
        return new HubTemporaryData();
    }

    @Override
    public void add(Profile profile) {
        Player player = profile.getPlayer();

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

    private void snow() {
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (Profile p : getPlayerList()) {
//                    sendParticle(EnumParticle.FIREWORKS_SPARK, p.getPlayer(), 0.0f, 150);
//                }
//            }
//        }.runTaskTimer(plugin, 10, 10);
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
