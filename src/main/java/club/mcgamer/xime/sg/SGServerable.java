package club.mcgamer.xime.sg;

import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.design.bossbar.SGBossbarAdapter;
import club.mcgamer.xime.sg.design.sidebar.SGSidebarAdapter;
import club.mcgamer.xime.sg.runnable.*;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
public class SGServerable extends Serverable {

    public static final String LOBBY_NAME = "Lobby";

    @Setter private Location lobbyLocation;

    private AbstractGameRunnable currentRunnable;
    @Setter private GameTimer gameTimer;
    @Setter private GameSettings gameSettings;
    private GameState gameState;

    private final ArrayList<Profile> tributeList = new ArrayList<>();
    private final ArrayList<Profile> spectatorList = new ArrayList<>();
    private final ArrayList<String> fallenTributes = new ArrayList<>();

    @Setter private MapPool mapPool;
    @Setter private VoteableMap mapWinner;

    public SGServerable() {
        super();

        setup();

        setSidebarAdapter(new SGSidebarAdapter());
        setBossbarAdapter(new SGBossbarAdapter());
    }

    public TemporaryData createTemporaryData() {
        return new SGTemporaryData();
    }

    public void setup() {
        setMaxPlayers(24);
        setWorld(toString() + "-" + LOBBY_NAME, LOBBY_NAME);
        setMapData(MapData.load(LOBBY_NAME));
        setGameState(GameState.LOBBY);
    }

    public void reset() {
        setMaxPlayers(24);
        setGameState(GameState.LOBBY);
        overrideWorld(Bukkit.getWorld(toString() + "-" + LOBBY_NAME));

        tributeList.clear();
        spectatorList.clear();

        gameSettings.setSilentJoinLeave(true);
        new ArrayList<>(getPlayerList()).forEach(this::add);

        Bukkit.unloadWorld(toString(), false);
    }

    public void setSpectating(Profile profile) {
        Player player = profile.getPlayer();
        tributeList.remove(profile);
        spectatorList.add(profile);

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

        PlayerUtil.refresh(profile);
        PlayerUtil.unsetGamemode(profile);
        player.setVelocity(new Vector(0, 0.5, 0.0));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Short.MAX_VALUE, 255, false, false));
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
