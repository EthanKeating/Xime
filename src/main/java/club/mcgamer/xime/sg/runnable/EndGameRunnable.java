package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.FireworkUtil;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

public class EndGameRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    @Getter private final Optional<Profile> gameWinner;

    public EndGameRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        String prefix = serverable.getPrefix();
        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getEndGameTime());

        gameWinner = serverable.getTributeList().stream()
                .filter(profile -> profile.getTemporaryData() instanceof SGTemporaryData)
                .max(Comparator.comparingInt(profile -> ((SGTemporaryData) profile.getTemporaryData()).getKillCount()));

        serverable.announce("&aThe games have ended!");
        if (gameWinner.isPresent()) {
            Profile gameWinnerProfile = gameWinner.get();

            if (gameWinnerProfile.getTemporaryData() instanceof SGTemporaryData temporaryData) {
                PlayerData playerData = gameWinnerProfile.getPlayerData();

                if (!(serverable instanceof SGMakerServerable)) {
                    playerData.setSgGamesWon(playerData.getSgGamesWon() + 1);
                    playerData.setSgGamesPlayed(playerData.getSgGamesPlayed() + 1);

                    if (temporaryData.getKillCount() > playerData.getSgMostKills())
                        playerData.setSgMostKills(temporaryData.getKillCount());

                    long lifeDuration = System.currentTimeMillis() - temporaryData.getLifeStart();

                    playerData.setSgLifeSpan(playerData.getSgLifeSpan() + lifeDuration);
                    if (playerData.getSgLongestLifeSpan() < lifeDuration)
                        playerData.setSgLongestLifeSpan(lifeDuration);

                    if (temporaryData.getBounty() > 0) {
                        playerData.setSgPoints(playerData.getSgPoints() + temporaryData.getBounty());
                        gameWinnerProfile.sendMessage(String.format(prefix + "&aYou've gained &8[&e%s&8] &aextra points from your bounty!", temporaryData.getBounty()));
                    }
                }
            }
        }

        gameWinner.ifPresent(profile -> {
            if(gameSettings.isRandomizeNames())
                plugin.getDisguiseHandler().undisguise(profile);

            serverable.announce(String.format("&a%s &ahas won the Survival Games!", profile.getDisplayName()));
        });
        serverable.getWorld().setTime(18000);

        MapData mapData = serverable.getMapData();
        List<MapLocation> fireworkLocations = new ArrayList<>(mapData.getSpawnLocations());
        if (mapData.getDmLocations().get(0) != mapData.getSpawnLocations().get(0))
            fireworkLocations.addAll(mapData.getDmLocations());

        fireworkLocations.stream()
                .map(mapLocation -> mapLocation.toBukkit(serverable.getWorld()))
                .forEach(FireworkUtil::sendFirework);
    }

    public void run() {
        int currentTime = gameTimer.decrement();

        if (currentTime == 0) {
            cancel();
            return;
        }
    }

    public void cancel() {
        super.cancel();
        serverable.setGameState(GameState.CLEANUP);
    }

}
