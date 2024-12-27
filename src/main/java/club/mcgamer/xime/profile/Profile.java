package club.mcgamer.xime.profile;

import club.mcgamer.xime.design.bossbar.BossbarImpl;
import club.mcgamer.xime.design.sidebar.SidebarImpl;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.profile.impl.CombatTagData;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.util.Skin;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearTitles;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.title.Title;
import com.lunarclient.apollo.module.title.TitleModule;
import com.lunarclient.apollo.module.title.TitleType;
import com.lunarclient.apollo.player.ApolloPlayer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Profile {

    private final UUID uuid;
    private final User user;
    private final Skin skin;

    @Setter private Serverable serverable;

    private final SidebarImpl sidebarImpl;
    private final BossbarImpl bossbarImpl;

    @Setter private TemporaryData temporaryData;
    private final CombatTagData combatTagData;
    @Setter private DisguiseData disguiseData;
    private final boolean legacy;

    public Profile(final UUID uuid) {
        this.uuid = uuid;

        this.user = PacketEvents.getAPI().getProtocolManager().getUser(
                SpigotReflectionUtil.getChannel(getPlayer())
        );

        legacy = user
                .getClientVersion()
                .getProtocolVersion() == 5;

        sidebarImpl = new SidebarImpl(this);
        bossbarImpl = new BossbarImpl(this);

        combatTagData = new CombatTagData();

        EntityPlayer playerNMS = ((CraftPlayer) getPlayer()).getHandle();
        GameProfile profile = playerNMS.getProfile();
        if (profile.getProperties().get("textures").iterator().hasNext()) {
            Property property = profile.getProperties().get("textures").iterator().next();

            this.skin = new Skin(
                    property.getValue(),
                    property.getSignature()
            );
        } else {
            this.skin = Skin.DEAD;
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getName() {
        if (disguiseData != null)
            return disguiseData.getName();

        return getPlayer().getName();
    }

    public String getDisplayName() {
        if (disguiseData != null)
            return ChatColor.DARK_GREEN + disguiseData.getName();

        //replace with Disguise displayname aswell
        return ChatColor.DARK_GREEN + getPlayer().getDisplayName();
    }

    public String getChatColor() {
        return "&f";
    }

    public Profile sendMessage(String message) {
        getUser().sendMessage(TextUtil.translate(message));
        return this;
    }

    public void clearTitle() {

        if (isLegacy()) {
            TitleModule titleModule = Apollo.getModuleManager().getModule(TitleModule.class);
            Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(getUuid());
            apolloPlayerOpt.ifPresent(titleModule::resetTitles);

            return;
        }
        getUser().sendPacket(new WrapperPlayServerClearTitles(true));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int duration, int fadeOut) {
        if (isLegacy()) {
            TitleModule titleModule = Apollo.getModuleManager().getModule(TitleModule.class);

            Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(getUuid());;
            apolloPlayerOpt.ifPresent(apolloPlayer -> titleModule.displayTitle(apolloPlayer, Title.builder()
                    .type(TitleType.TITLE)
                    .message(Component.text(TextUtil.translate(title)))
                    .scale(1.0f)
                    .displayTime(Duration.ofMillis(duration * 50L))
                    .fadeInTime(Duration.ofMillis(Math.max(1, fadeIn * 50L)))
                    .fadeOutTime(Duration.ofMillis(Math.max(1, fadeOut * 50L)))
                    .build()));
            apolloPlayerOpt.ifPresent(apolloPlayer -> titleModule.displayTitle(apolloPlayer, Title.builder()
                    .type(TitleType.SUBTITLE)
                    .message(Component.text(TextUtil.translate(subTitle)))
                    .scale(1.0f)
                    .displayTime(Duration.ofMillis(duration * 50L))
                    .fadeInTime(Duration.ofMillis(Math.max(1, fadeIn * 50L)))
                    .fadeOutTime(Duration.ofMillis(Math.max(1, fadeOut * 50L)))
                    .build()));

            return;
        }
        getUser().sendTitle(TextUtil.translate(title), TextUtil.translate(subTitle), fadeIn, duration, fadeOut);
    }

}
