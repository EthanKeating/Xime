package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.Pair;

public class LobbyRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer = new GameTimer();
    private final GameSettings gameSettings = new GameSettings();


    public LobbyRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        serverable.setGameSettings(this.gameSettings);
        serverable.setGameTimer(this.gameTimer);
        serverable.setMapPool(new MapPool());

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getLobbyLength());
        //Initialize all data
    }

    public void run() {
        int playerCount = serverable.getPlayerList().size();

        if (playerCount == 0) {
            gameTimer.reset();
            return;
        }

        int currentTime = gameTimer.decrement();

        if (currentTime == 0) {
            if (gameSettings.getMinimumPlayers() > playerCount) {
                String notEnoughPlayers = "&4Not enough players, timer reset.";

                serverable.announce(notEnoughPlayers);
                serverable.announceTitle("", notEnoughPlayers, 10, 30, 10);
                gameTimer.reset();
                return;
            }
            cancel();
            return;
        }


        if (currentTime <= 5 || currentTime == 10 || currentTime % 30 == 0) {
            Pair<String, String> significantUnit = gameTimer.toSignificantUnit();

            serverable.announce(String.format("&8[&e%s&8] &c%s until lobby ends!", significantUnit.getKey(), significantUnit.getValue()));
        }
        //Show vote messages
        if (currentTime % 30 == 0) {
            serverable.getPlayerList().forEach(this::sendVotes);
        }
    }

    public void sendVotes(Profile profile) {
        serverable.getMapPool().getRandomMaps().forEach((id, voteableMap) -> {
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
