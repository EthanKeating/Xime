package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapPool;
import club.mcgamer.xime.map.impl.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.Pair;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LobbyRunnable extends AbstractGameRunnable {

    protected final XimePlugin plugin;
    protected final SGServerable serverable;
    protected final GameTimer gameTimer = new GameTimer();
    protected final GameSettings gameSettings = new GameSettings();


    public LobbyRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        serverable.setGameSettings(this.gameSettings);
        serverable.setGameTimer(this.gameTimer);
        serverable.setMapPool(new MapPool(plugin));
        serverable.setMaxPlayers(this.gameSettings.getMaximumPlayers());

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getLobbyLength());
        //Initialize all data
    }

    public void run() {
        int playerCount = serverable.getPlayerList().size();

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
        if (currentTime <= 10)
            serverable.announceTitle("&6" + currentTime, "", 0, 30, 0);

        //Show vote messages
        if (currentTime % 30 == 0) {
            serverable.getPlayerList().forEach(this::sendVotes);
        }

        if (currentTime == 1 && gameSettings.getMinimumPlayers() <= playerCount) {
            Bukkit.getScheduler().runTaskLater(plugin, this::cancel, 11L);
        }
    }

    public void sendVotes(Profile profile) {
        int totalVotes = serverable.getMapPool().getRandomMaps().values().stream()
                .mapToInt(VoteableMap::getVotes)
                .sum();

        serverable.getMapPool().getRandomMaps().forEach((id, voteableMap) -> {
            MapData mapData = voteableMap.getMapData();

            double chancePercentage = (double) voteableMap.getVotes() / totalVotes * 100;
            String prefix = serverable.getPrefix();

            NumberFormat nf = new DecimalFormat("##.##");

            if (serverable.getMapPool().isMapChances())
                profile.sendMessage(String.format(
                        prefix + "&a%s &8> | &e%s &7%s &8| &e%s%% &7Chance &8| &2%s",
                        id,
                        voteableMap.getVotes(),
                        voteableMap.getVotes() == 1 ? "Vote" : "Votes",
                        nf.format(Double.isNaN(chancePercentage) ? 0 : chancePercentage),
                        mapData.getMapName()));
            else
                profile.sendMessage(String.format(
                        prefix + "&a%s &8> | &e%s &7%s &8| &2%s",
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
