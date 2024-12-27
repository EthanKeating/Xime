package club.mcgamer.xime.design.bossbar;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter @Setter @Accessors(chain = true)
public class BossbarImpl {

    private static final int[] ENTITY_IDS = new int[] {SpigotReflectionUtil.generateEntityId(), SpigotReflectionUtil.generateEntityId()};

    private final Profile profile;
    private String title = "&7";
    private float health = 300;
    private boolean hidden;

    public BossbarImpl(Profile profile) {
        this.profile = profile;
    }

    public void tick(BossbarAdapter adapter) {
        if (!adapter.getTitle(profile).equals(title)) title = adapter.getTitle(profile);
        if (adapter.getHealth(profile) != health) health = adapter.getHealth(profile);
        if (adapter.isHidden(profile) != hidden) {
            hidden = adapter.isHidden(profile);
            if (hidden) {
                profile.getUser().sendPacket(new WrapperPlayServerDestroyEntities(ENTITY_IDS));
            }
        }

        if (!hidden) {
            respawn(0);
            respawn(1);
        }
    }

    private void respawn(int id) {
        User user = profile.getUser();
        Player bukkitPlayer = profile.getPlayer();

        if (bukkitPlayer == null) return;

        Location location = (id == 0) ?
                bukkitPlayer.getEyeLocation().add(bukkitPlayer.getLocation().getDirection().multiply(26)).add(0, 3, 0)
                : bukkitPlayer.getEyeLocation().add(bukkitPlayer.getLocation().getDirection().multiply(-26));

        user.sendPacket(new WrapperPlayServerSpawnLivingEntity(
                ENTITY_IDS[id],
                UUID.randomUUID(),
                EntityTypes.WITHER,
                new Vector3d(location.getX(), location.getY(), location.getZ()),
                0,
                0,
                0,
                new Vector3d(0.0, 0.0, 0.0),
                buildData()));
    }

    private List<EntityData> buildData() {
        return Arrays.asList(
                new EntityData(0, EntityDataTypes.BYTE, (byte) 0x20),
                new EntityData(1, EntityDataTypes.SHORT, (short) 300),
                new EntityData(2, EntityDataTypes.STRING, TextUtil.translate(title)),
                new EntityData(3, EntityDataTypes.BYTE, (byte) 1),
                new EntityData(6, EntityDataTypes.FLOAT, health),
                new EntityData(7, EntityDataTypes.INT, Color.BLACK.asRGB()),
                new EntityData(8, EntityDataTypes.BYTE, (byte) 0),
                new EntityData(15, EntityDataTypes.BYTE, (byte) 1),
                new EntityData(20, EntityDataTypes.INT, Integer.MAX_VALUE)
        );
    }

    public static float getHealthScaled(double percentDecimal) {
        return (float) percentDecimal * 300F;
    }

}

