package club.mcgamer.xime.sgmaker;

import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.*;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.runnable.MakerLobbyRunnable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

@Getter @Setter
public class SGMakerServerable extends SGServerable {

    private final Profile owner;
    private final VoteableMap selectedMap = !MapPool.getMapIdentifiers().isEmpty() ? MapPool.get(MapPool.getMapIdentifiers().get(0)) : null;

    private final String secret = UUID.randomUUID().toString().substring(0, 5);

    public SGMakerServerable(Profile owner) {
        super();

        this.owner = owner;
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

        if (Bukkit.getWorld(toString() + "-" + LOBBY_NAME) != null) {
            Bukkit.getWorld(toString() + "-" + LOBBY_NAME).getPlayers().forEach(loopPlayer -> loopPlayer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()));
            Bukkit.unloadWorld(toString() + "-" + LOBBY_NAME, false);
        }

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
        overrideWorld(Bukkit.getWorld(toString() + "-" + LOBBY_NAME));
        setMapData(MapData.load(LOBBY_NAME));

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
        });

        gameSettings.setSilentJoinLeave(false);;
    }

}
