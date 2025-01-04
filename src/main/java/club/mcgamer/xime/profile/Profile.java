package club.mcgamer.xime.profile;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.design.bossbar.BossbarImpl;
import club.mcgamer.xime.design.sidebar.SidebarImpl;
import club.mcgamer.xime.design.tag.TagImpl;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.profile.impl.CombatTagData;
import club.mcgamer.xime.profile.impl.GeneralData;
import club.mcgamer.xime.profile.impl.GeoLocationData;
import club.mcgamer.xime.profile.impl.SidebarType;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.util.DisguiseUtil;
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

    @Setter private Rank rank;

    @Setter private Serverable serverable;

    private final String name;

    private final SidebarImpl sidebarImpl;
    private final BossbarImpl bossbarImpl;
    private final TagImpl tagImpl;

    @Setter private SidebarType sidebarType;
    @Setter private TemporaryData temporaryData;
    private final GeneralData generalData;
    private final CombatTagData combatTagData;
    private GeoLocationData geoLocationData;
    @Setter private DisguiseData disguiseData;

    private final boolean legacy;

    public Profile(final UUID uuid) {
        this.uuid = uuid;
        this.user = PacketEvents.getAPI()
                .getProtocolManager()
                .getUser(SpigotReflectionUtil.getChannel(getPlayer()));
        this.legacy = user.getClientVersion().getProtocolVersion() == 5;
        this.name = getPlayer().getName();

        this.sidebarImpl = new SidebarImpl(this);
        this.bossbarImpl = new BossbarImpl(this);
        this.tagImpl = new TagImpl(this);
        this.combatTagData = new CombatTagData();
        this.generalData = new GeneralData();
        this.sidebarType = SidebarType.DEFAULT;

        this.skin = DisguiseUtil.getSkin(getPlayer());
        this.rank = RankHandler.DEFAULT_RANK;

        //TODO: make this better add the async task in the data population, and auto populate every field with default data
        Bukkit.getScheduler().runTaskAsynchronously(XimePlugin.getProvidingPlugin(XimePlugin.class), () -> {
            geoLocationData = new GeoLocationData(getPlayer().getAddress().getAddress());
        });
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getName() {
        if (disguiseData != null)
            return disguiseData.getName();

        return name;
    }

    public String getDisplayName() {

        if (disguiseData != null)
            return disguiseData.getRank().getColor() + disguiseData.getName();

        if (rank.getName().equalsIgnoreCase("Developer"))
            return TextUtil.toRainbow(getPlayer().getName());

        //replace with Disguise displayname aswell
        return TextUtil.translate(rank.getColor()) + getPlayer().getDisplayName();
    }

    public String getChatColor() {

        return "&f";
    }

    public Rank getRank() {
        if (disguiseData != null)
            return disguiseData.getRank();

        return rank;
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
