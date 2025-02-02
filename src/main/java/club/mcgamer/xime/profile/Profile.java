package club.mcgamer.xime.profile;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.design.bossbar.BossbarImpl;
import club.mcgamer.xime.design.sidebar.SidebarImpl;
import club.mcgamer.xime.design.tag.TagImpl;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.lang.impl.Language;
import club.mcgamer.xime.profile.data.impl.ProfileStatus;
import club.mcgamer.xime.profile.data.impl.ReplyData;
import club.mcgamer.xime.profile.data.persistent.GeoLocationData;
import club.mcgamer.xime.profile.data.temporary.CombatTagData;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.util.DisguiseUtil;
import club.mcgamer.xime.util.Skin;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearTitles;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.title.Title;
import com.lunarclient.apollo.module.title.TitleModule;
import com.lunarclient.apollo.module.title.TitleType;
import com.lunarclient.apollo.player.ApolloPlayer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Profile {

    private final XimePlugin plugin;

    private ProfileStatus profileStatus = ProfileStatus.LOADING;

    private UUID uuid;
    private User user;
    private Skin skin;

    @Setter private String chatColor = "&f";

    @Setter private Serverable serverable;

    private String name;
    private Rank rank;

    private Language language;
    private SidebarImpl sidebarImpl;
    private BossbarImpl bossbarImpl;
    private TagImpl tagImpl;

    @Setter private TemporaryData temporaryData;
    private PlayerData playerData;
    private CombatTagData combatTagData;
    private CooldownData cooldownData;
    private GeoLocationData geoLocationData = GeoLocationData.DEFAULT;
    @Setter private DisguiseData disguiseData;
    private ReplyData replyData;

    private boolean legacy;

    public Profile(final UUID uuid, XimePlugin plugin) {
        this.plugin = plugin;
        this.uuid = uuid;

        this.playerData = plugin.getDataHandler().getPlayerData(uuid);

        setRank(playerData.getUserRank());
    }

    public void complete() {
        this.user = PacketEvents.getAPI()
                .getProtocolManager()
                .getUser(SpigotReflectionUtil.getChannel(getPlayer()));
        this.legacy = user.getClientVersion().getProtocolVersion() == 5;
        this.name = getPlayer().getName();

        this.language = plugin.getLanguageHandler().getLanguage("en");
        this.sidebarImpl = new SidebarImpl(this);
        this.bossbarImpl = new BossbarImpl(this);
        this.tagImpl = new TagImpl(this);
        this.combatTagData = new CombatTagData();
        this.cooldownData = new CooldownData(this);
        this.replyData = new ReplyData();

        this.skin = DisguiseUtil.getSkin(getPlayer());
        this.profileStatus = ProfileStatus.COMPLETE;

        updatePermissions();
        playerData.setUserName(getNameBypassDisguise());
        playerData.setDisplayName(rank.getColor() + getNameBypassDisguise());
        getPlayer().setDisplayName(rank.getColor() + getNameBypassDisguise());
    }

    private void updatePermissions() {
        Player player = getPlayer();

        player.getEffectivePermissions().forEach(permissionAttachmentInfo -> {
            if (permissionAttachmentInfo != null && permissionAttachmentInfo.getAttachment() != null && permissionAttachmentInfo.getAttachment().getPlugin() == plugin)
                player.removeAttachment(permissionAttachmentInfo.getAttachment());
        });

        plugin.getRankHandler().getRankList()
                .stream()
                .filter(otherRank -> plugin.getRankHandler().getRankList().indexOf(otherRank) >= plugin.getRankHandler().getRankList().indexOf(getRankBypassDisguise()))
                        .forEach(otherRank -> {
                            otherRank.getPermissions().forEach(permission -> player.addAttachment(plugin, permission, true));
                        });


    }

    public void setRank(String rankName) {
        setRank(plugin.getRankHandler().getRank(rankName));
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        playerData.setUserRank(rank.getName());

        if (getPlayer() != null) {
            playerData.setDisplayName(rank.getColor() + getNameBypassDisguise());
            playerData.setUserName(getNameBypassDisguise());
            getPlayer().setDisplayName(rank.getColor() + getNameBypassDisguise());
            updatePermissions();
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getName() {
        if (getPlayer() == null)
            return "";

        if (disguiseData != null)
            return disguiseData.getName();

        return name;
    }

    public String getNameBypassDisguise() {
        if (getPlayer() == null)
            return "";

        return name;
    }

    public PlayerData getMockOrRealPlayerData() {
        if (disguiseData != null && disguiseData.getMockData() != null)
            return disguiseData.getMockData();

        return playerData;
    }

    public String getDisplayName() {

        if (getPlayer() == null)
            return "";

        if (disguiseData != null)
            return disguiseData.getRank().getColor() + disguiseData.getName();

        if (rank.getName().equalsIgnoreCase("Developer"))
            return TextUtil.toRainbow(getPlayer().getName());

        //replace with Disguise displayname aswell
        return TextUtil.translate(rank.getColor() + getPlayer().getDisplayName());
    }

    public String getDisplayNameBypassDisguise() {
        if (getPlayer() == null)
            return "";

        if (rank.getName().equalsIgnoreCase("Developer"))
            return TextUtil.toRainbow(name);

        //replace with Disguise displayname aswell
        return TextUtil.translate(rank.getColor() + name);
    }

    public String getChatColor() {
        if (getPlayer() == null)
            return "";

        if (disguiseData != null && rank == RankHandler.DEFAULT_RANK)
            return TextUtil.translate("&f");

        return TextUtil.translate(chatColor);
    }

    public Rank getRank() {

        if (disguiseData != null)
            return disguiseData.getRank();

        return rank;
    }

    public Rank getRankBypassDisguise() {
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

    public void sendAction(String text) {
        getUser().sendMessage(Component.text(TextUtil.translate(text)), ChatTypes.GAME_INFO);
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
