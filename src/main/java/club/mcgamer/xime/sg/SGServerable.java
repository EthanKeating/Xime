package club.mcgamer.xime.sg;

import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.design.bossbar.SGBossbarAdapter;
import club.mcgamer.xime.sg.design.sidebar.SGSidebarAdapter;
import club.mcgamer.xime.sg.runnable.*;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.EvictingList;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.PlayerUtil;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class SGServerable extends Serverable {

    public static final String LOBBY_NAME = "Lobby";

    protected AbstractGameRunnable currentRunnable;
    @Setter protected GameTimer gameTimer;
    @Setter protected GameSettings gameSettings;
    protected GameState gameState;

    protected final ArrayList<Profile> tributeList = new ArrayList<>();
    protected final ArrayList<Profile> spectatorList = new ArrayList<>();
    protected final ArrayList<String> fallenTributes = new ArrayList<>();

    protected final Set<MapLocation> openedChestLocations = new HashSet<>();

    protected final ArrayList<Pair<ItemStack, AtomicInteger>> sponsorItems = new ArrayList<>();

    @Setter protected MapPool mapPool;
    @Setter protected VoteableMap mapWinner;

    private final EvictingList<String> previousMapNames = new EvictingList<>(5);

    public SGServerable() {
        super();

        setup();

        setSidebarAdapter(new SGSidebarAdapter());
        setBossbarAdapter(new SGBossbarAdapter());
    }

    public void add(Profile profile) {
        Player player = profile.getPlayer();

        if (getPlayerList().size() >= getMaxPlayers()) {
            profile.sendMessage("&cThat server is full.");

            if (!(profile.getServerable() instanceof HubServerable))
                plugin.getServerHandler().getFallback().add(profile);

            return;
        }

        if (!isJoinable()) {
            profile.sendMessage("&cThis server is not currently joinable");
            return;
        }

        if (profile.getServerable() != null)
            profile.getServerable().remove(profile);


        //TODO: Fix the weird spectator bug
        getPlayerList().stream().map(Profile::getPlayer).forEach(loopPlayer -> {
            loopPlayer.showPlayer(player);
            player.showPlayer(loopPlayer);
        });

        getPlayerList().remove(profile);
        getPlayerList().add(profile);
        profile.setServerable(this);
        profile.setTemporaryData(createTemporaryData());
        Bukkit.getPluginManager().callEvent(new ServerJoinEvent(profile, profile.getServerable()));

    }

    public Location getLobbyLocation() {
        Location location = getMapData().getCenterLocation().toBukkit(Bukkit.getWorld(LOBBY_NAME + "-1"));
        location.setYaw(-90);
        return location;

    }

    public TemporaryData createTemporaryData() {
        return new SGTemporaryData();
    }

    public void setup() {
        World world = Bukkit.getWorld(LOBBY_NAME + "-1");

        setWorldName(LOBBY_NAME + "-1");
        overrideWorld(world);

        //setWorld(toString() + "-" + LOBBY_NAME, LOBBY_NAME);
        setMapData(MapData.load(LOBBY_NAME));
        setGameState(GameState.LOBBY);
        setJoinable(true);

        populateSponsor();
    }

    public void reset() {
        setGameState(GameState.LOBBY);
        setWorldName(LOBBY_NAME + "-1");
        overrideWorld(Bukkit.getWorld(LOBBY_NAME + "-1"));
        setMapData(MapData.load(LOBBY_NAME));

        populateSponsor();

        tributeList.clear();
        spectatorList.clear();
        fallenTributes.clear();

        gameSettings.setSilentJoinLeave(true);

        new ArrayList<>(getPlayerList()).forEach(profile -> {
            profile.sendMessage("Sending you to a hub...");
            plugin.getServerHandler().getFallback().add(profile);
        });

        if(Bukkit.getWorld(toString()) != null) {
            Bukkit.getWorld(toString()).getPlayers().forEach(loopPlayer -> loopPlayer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(toString(), false);
        }
        gameSettings.setSilentJoinLeave(false);
    }

    protected void populateSponsor() {

        sponsorItems.clear();
        sponsorItems.addAll(Arrays.asList(
                new Pair<>(new ItemBuilder(Material.ENDER_PEARL).build(), new AtomicInteger(150)),
                new Pair<>(new ItemBuilder(Material.IRON_INGOT).build(), new AtomicInteger(125)),
                new Pair<>(new ItemBuilder(Material.ARROW).amount(5).build(), new AtomicInteger(75)),
                new Pair<>(new ItemBuilder(Material.EXP_BOTTLE).amount(2).build(), new AtomicInteger(125)),
                new Pair<>(new ItemBuilder(Material.PORK).build(), new AtomicInteger(50)),
                new Pair<>(new ItemBuilder(Material.BOW).build(), new AtomicInteger(100)),
                new Pair<>(new ItemBuilder(Material.FLINT_AND_STEEL).build(), new AtomicInteger(125)),
                new Pair<>(new ItemBuilder(Material.MUSHROOM_SOUP).build(), new AtomicInteger(60)),
                new Pair<>(new ItemBuilder(Material.CAKE).build(), new AtomicInteger(75))
        ));
    }

    public void setSpectating(Profile profile) {
        Player player = profile.getPlayer();
        tributeList.remove(profile);
        spectatorList.add(profile);

        if (gameSettings.isRandomizeNames())
            plugin.getDisguiseHandler().undisguise(profile);

        getTributeList().forEach(loopProfile -> {
            Player loopPlayer = loopProfile.getPlayer();

            loopPlayer.hidePlayer(player);
            player.showPlayer(loopPlayer);
        });

        getSpectatorList().forEach(loopProfile -> {
            Player loopPlayer = loopProfile.getPlayer();

            loopPlayer.showPlayer(player);
            player.showPlayer(loopPlayer);
        });

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (profile.getPlayer() == null)
                return;
            if (profile.getServerable() == null)
                return;
            if (profile.getServerable() != this)
                return;

            PlayerUtil.refresh(profile);
            PlayerUtil.unsetGamemode(profile);
            player.setVelocity(new Vector(0, 0.5, 0.0));
            player.setAllowFlight(true);
            PlayerUtil.setFlying(profile);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Short.MAX_VALUE, 255, false, false));
        }, 1L);
    }

    public void setGameState(GameState newState) {
        gameState = newState;

        switch(newState) {
            case LOBBY:
                currentRunnable = new LobbyRunnable(this, plugin); break;
            case PREGAME:
                currentRunnable = new PreGameRunnable(this, plugin); break;
            case LIVEGAME:
                currentRunnable = new LiveGameRunnable(this, plugin); break;
            case PREDEATHMATCH:
                currentRunnable = new PreDeathmatchRunnable(this, plugin); break;
            case DEATHMATCH:
                currentRunnable = new DeathmatchRunnable(this, plugin); break;
            case ENDGAME:
                currentRunnable = new EndGameRunnable(this, plugin); break;
            case CLEANUP:
                currentRunnable = new CleanupRunnable(this, plugin); break;
            default:
                currentRunnable = null;
        }

        if (currentRunnable != null)
            currentRunnable.runTaskTimer(plugin, 20L, 20L);
    }

    @SneakyThrows
    public void forceGameState(GameState newState) {

        if (currentRunnable != null)
            currentRunnable.forceCancel();

        setGameState(newState);
    }

    public void announce(String text) {
        announceRaw("&8[&6MCSG&8] &f" + text);
    }

    public void announceRaw(String text) {
        getPlayerList().forEach(profile -> profile.sendMessage(text));
    }

    public void announceRawUncoloured(String text) {
        getPlayerList().stream().map(Profile::getPlayer).forEach(player -> player.sendMessage(text));
    }

    public void announceSound(Sound sound, float volume, float pitch) {
        getPlayerList().stream()
                .map(Profile::getPlayer)
                .forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
    }

    public void announceTitle(String title, String subTitle, int fadeIn, int duration, int fadeOut) {
        getPlayerList().forEach(profile -> profile.sendTitle(title, subTitle, fadeIn, duration, fadeOut));
    }

}
