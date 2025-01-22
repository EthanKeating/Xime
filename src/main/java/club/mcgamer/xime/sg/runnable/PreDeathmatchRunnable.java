package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.MathUtil;
import club.mcgamer.xime.util.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PreDeathmatchRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    public PreDeathmatchRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getPreDeathmatchTime());

        List<Profile> allPlayers = new ArrayList<>(serverable.getTributeList());

        List<Integer> spawnIndexes = MathUtil.distributeObjects(serverable.getMapData().getDmLocations().size(), allPlayers.size());
        List<MapLocation> spawnLocations = serverable.getMapData().getDmLocations();
        Location centerLocation = serverable.getMapData().getDmCenterLocation().toBukkit(serverable.getWorld()).add(0.0, 0.25, 0.0);

        serverable.getSpectatorList().stream().map(Profile::getPlayer).forEach(player -> player.teleport(centerLocation));
        for (int i = 0; i < allPlayers.size(); i++) {
            int spawnIndex = spawnIndexes.get(i % spawnIndexes.size());

            Location worldLocation = MathUtil.lookAt(spawnLocations.get(spawnIndex).toBukkit(serverable.getWorld()), centerLocation);

            Profile profile = allPlayers.get(i);
            Player player = profile.getPlayer();
            SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

            player.teleport(worldLocation);
            temporaryData.setPedistalLocation(worldLocation);
        }
    }

    public void run() {
        int currentTime = gameTimer.decrement();
        Pair<String, String> sigUnit = gameTimer.toSignificantUnit();

        if (currentTime == 0) {
            cancel();
            return;
        }

        if (currentTime == gameTimer.getInitialTime()) {
            serverable.announce(String.format("&4Please allow &8[&e%s&8] &4%s for all players to load the map.", sigUnit.getKey(), sigUnit.getValue()));
            return;
        }

        if (currentTime <= 5) {
            serverable.announce(String.format("&8[&e%s&8] &c%s until deathmatch!", sigUnit.getKey(), sigUnit.getValue()));
        }
    }

    public void cancel() {
        super.cancel();

        serverable.setGameState(GameState.DEATHMATCH);
    }

}
