package club.mcgamer.xime.profile;

import club.mcgamer.xime.design.bossbar.BossbarImpl;
import club.mcgamer.xime.design.sidebar.SidebarImpl;
import club.mcgamer.xime.profile.impl.CombatTagData;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class Profile {

    private final UUID uuid;
    private final User user;

    @Setter private Serverable serverable;

    private final SidebarImpl sidebarImpl;
    private final BossbarImpl bossbarImpl;

    @Setter private TemporaryData temporaryData;
    private final CombatTagData combatTagData;
    private boolean legacy;

    public Profile(final UUID uuid) {
        this.uuid = uuid;
        this.user = PacketEvents.getAPI().getProtocolManager().getUser(
                ((CraftPlayer) getPlayer())
                        .getHandle()
                        .playerConnection
                        .networkManager
                        .channel
        );

        legacy = user
                .getClientVersion()
                .getProtocolVersion() == 5;

        sidebarImpl = new SidebarImpl(this);
        bossbarImpl = new BossbarImpl(this);

        combatTagData = new CombatTagData();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getDisplayName() {
        //replace with Disguise displayname aswell
        return getPlayer().getDisplayName();
    }

    public String getChatColor() {
        return "&f";
    }

    public Profile sendMessage(String message) {
        getUser().sendMessage(TextUtil.translate(message));
        return this;
    }

}
