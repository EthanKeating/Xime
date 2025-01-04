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
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
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

        if (!isJoinable()) {
            profile.sendMessage("&cThis server is not currently joinable");
            return;
        }

        if (profile.getServerable() != null)
            profile.getServerable().remove(profile);

        getPlayerList().stream().forEach(loopProfile -> {
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
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Profile p : getPlayerList()) {
                    for (int i = 0; i < 125; i++) {
                        Location loc = p.getPlayer().getLocation().add(
                                ThreadLocalRandom.current().nextDouble(-35, 35),
                                ThreadLocalRandom.current().nextDouble(0.0, 35),
                                ThreadLocalRandom.current().nextDouble(-35, 35));
                        p.getPlayer().playEffect(loc, Effect.FIREWORKS_SPARK, 0);
                    }
                }
            }
        }.runTaskTimer(plugin, 10, 10);
    }

}
