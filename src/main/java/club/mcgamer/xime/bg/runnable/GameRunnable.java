package club.mcgamer.xime.bg.runnable;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.leaderboard.LeaderboardEntry;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

@Getter
public class GameRunnable extends BukkitRunnable {

    private final BGServerable serverable;

    public GameRunnable(BGServerable serverable) {
        this.serverable = serverable;

        getServerable().getPlayerList().forEach(serverable::setWaiting);
        getServerable().getWorld().setDifficulty(Difficulty.HARD);
    }

    @Override
    public void run() {
        GameTimer gameTimer = serverable.getGameTimer();
        if (serverable.isEmpty())
            return;

        HashMap<MapLocation, Long> placedBlocks = serverable.getPlacedBlocks();
        HashSet<MapLocation> removableBlocks = new HashSet<>();

        for(Map.Entry<MapLocation, Long> entry : placedBlocks.entrySet()) {
            MapLocation mapLocation = entry.getKey();
            long timeStamp = entry.getValue();

            if (System.currentTimeMillis() - timeStamp > 5 * 1000)
                removableBlocks.add(mapLocation);
        }

        for(MapLocation removableBlock : removableBlocks) {
            removableBlock.toBukkit(serverable.getWorld()).getBlock().setType(Material.AIR);
            placedBlocks.remove(removableBlock);
        }

        int gameTime = gameTimer.decrement();
        Pair<String, String> sigUnit = gameTimer.toSignificantUnit();

        serverable.updateLeaderboard();

        if (gameTime == 0) {
            if (!serverable.getSortedLeaderboard().isEmpty()) {
                LeaderboardEntry winnerEntry = serverable.getSortedLeaderboard().get(0);
                PlayerData winnerData = winnerEntry.getProfile().getPlayerData();

                serverable.announce(winnerData.getDisplayName() + " &ehas won the Battleground&8!");

                winnerData.setBgWins(winnerData.getBgWins() + 1);
            }

            serverable.reset();
            return;
        }
        serverable.getPlayerList().forEach(profile -> profile.getPlayer().setLevel(gameTime));

        if (gameTime % 60 == 0 || gameTime == 30 || gameTime == 15 || gameTime == 10 || gameTime <= 5)
            serverable.announce(String.format("&8[&6%s&8] &e%s until the next game&8!", sigUnit.getKey(), sigUnit.getValue()));
    }
}
