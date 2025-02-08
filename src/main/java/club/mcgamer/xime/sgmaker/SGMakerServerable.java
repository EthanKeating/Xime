package club.mcgamer.xime.sgmaker;

import club.mcgamer.xime.map.MapHandler;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapPool;
import club.mcgamer.xime.map.impl.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.*;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.config.MakerConfig;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.sgmaker.runnable.MakerLobbyRunnable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter @Setter
public class SGMakerServerable extends SGServerable {

    private final Profile owner;
    private final VoteableMap selectedMap;

    private PrivacyMode privacyMode = PrivacyMode.PRIVATE;
    private final String secret = UUID.randomUUID().toString().substring(0, 5);

    private HashMap<UUID, Long> invitedPlayers = new HashMap<>();

    public SGMakerServerable(Profile owner) {
        super();
        MapHandler mapHandler = getPlugin().getMapHandler();

        this.owner = owner;

        String firstMapIdentifier = new ArrayList<>(mapHandler.getMapPool().keySet()).get(0);
        MapData firstMapData = mapHandler.getMapPool().get(firstMapIdentifier);
        this.selectedMap = new VoteableMap(firstMapIdentifier, firstMapData, 0);

        if (Bukkit.getWorld(toString()) != null) {
            Serverable serverable = getPlugin().getServerHandler().getFallback();

            Bukkit.getWorld(toString()).getPlayers().stream().map(loopPlayer -> getPlugin().getProfileHandler().getProfile(loopPlayer)).forEach(serverable::add);
        }

        Bukkit.unloadWorld(toString(), false);
    }

    public void setInvited(Profile profile) {
        invitedPlayers.put(profile.getUuid(), System.currentTimeMillis());
    }

    public boolean isInvited(Profile profile) {
        if (!invitedPlayers.containsKey(profile.getUuid()))
            return false;

        long inviteTime = invitedPlayers.get(profile.getUuid());

        double elapsedSeconds = (System.currentTimeMillis() - inviteTime) / 1000.0d;
        double remainingSeconds = 60.0 - elapsedSeconds;

        return (remainingSeconds > 0);
    }

    public void applyConfig(MakerConfig makerConfig) {
        getGameSettings().setLootTable(makerConfig.getLootTable());
        getGameSettings().setLootStyle(makerConfig.getLootStyle());
    }

    @Override
    public void setGameState(GameState newState) {
        gameState = newState;

        switch(newState) {
            case LOBBY:
                currentRunnable = new MakerLobbyRunnable(this, plugin); break;
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

    @Override
    public void remove(Profile profile) {
        super.remove(profile);

        if (getPlayerList().isEmpty() && !gameSettings.isSilentJoinLeave())
            close();
    }

    public void close() {
        plugin.getServerHandler().getServerList().remove(this);
        gameSettings.setSilentJoinLeave(false);

        if(Bukkit.getWorld(toString()) != null) {
            Bukkit.getWorld(toString()).getPlayers().forEach(loopPlayer -> loopPlayer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(toString(), false);
        }

        if (currentRunnable != null)
            currentRunnable.forceCancel();
    }

    @Override
    public void reset() {
        setGameState(GameState.LOBBY);
        setWorldName(LOBBY_NAME + "-" + lobbyId);
        overrideWorld(Bukkit.getWorld(LOBBY_NAME + "-" + lobbyId));
        setMapData(MapData.load(LOBBY_NAME + lobbyId));

        populateSponsor();

        tributeList.clear();
        spectatorList.clear();
        fallenTributes.clear();

        gameSettings.setSilentJoinLeave(true);

        if(Bukkit.getWorld(toString()) != null) {
            Bukkit.getWorld(toString()).getPlayers().forEach(loopPlayer -> loopPlayer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(toString(), false);
        }

        new ArrayList<>(getPlayerList()).forEach(profile -> {
            plugin.getServerHandler().getFallback().add(profile);
            add(profile);

            if (profile.getDisguiseData() != null)
                plugin.getDisguiseHandler().undisguise(profile);
        });

        gameSettings.setSilentJoinLeave(false);
        gameSettings.getTeamProvider().setTeamType(gameSettings.getTeamProvider().getTeamType());
    }

}
