package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.Pair;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    private final MapPool mapPool;

    public LobbyRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();
        this.mapPool = new MapPool();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getLobbyLength());
        //Initialize all data
    }

    public void run() {
        int currentTime = gameTimer.decrement();

        GameSettings gameSettings = serverable.getGameSettings();
        if (currentTime == 0) {
            if (gameSettings.getMinimumPlayers() > serverable.getPlayerList().size()) {
                gameTimer.reset();
                return;
            }
            cancel();
            return;
        }


        if (currentTime <= 5 || currentTime == 10 || currentTime == 30 || currentTime % 60 == 0) {
            Pair<Integer, String> significantUnit = gameTimer.toSignificantUnit();

            serverable.announce(String.format("&8[&e%s&8] &c%s until lobby ends!", significantUnit.getKey(), significantUnit.getValue()));
        }
        //Show vote messages
        if (currentTime % 30 == 0) {
            serverable.getPlayerList().forEach(this::sendVotes);
        }
    }

    public void sendVotes(Profile profile) {
        mapPool.getRandomMaps().forEach((id, voteableMap) -> {
            MapData mapData = voteableMap.getMapData();

            profile.sendMessage(String.format(
                    "&8[&6MCSG&8] &a%s &8> | &e%s &7%s &8| &2%s",
                    id,
                    voteableMap.getVotes(),
                    voteableMap.getVotes() == 1 ? "Vote" : "Votes",
                    mapData.getMapName()));
        });
    }

    public void cancel() {
        super.cancel();
        serverable.setGameState(GameState.PREGAME);

        //Teleport players once map is loaded
    }

}
