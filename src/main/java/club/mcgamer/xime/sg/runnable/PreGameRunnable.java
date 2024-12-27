package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.MathUtil;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PreGameRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    public PreGameRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getPreGameLength());

        VoteableMap mapWinner = serverable.getMapPool().complete();
        MapData mapData = mapWinner.getMapData();

        serverable.setMapWinner(mapWinner);
        serverable.setMapData(mapData);

        serverable.setWorld(serverable.toString(), mapWinner.getMapIdentifier());
        if (mapData.getDmCenterLocation() == null)
            mapData.setDmCenterLocation(mapData.getCenterLocation());
        if (mapData.getDmLocations().isEmpty())
            mapData.getDmLocations().addAll(mapData.getSpawnLocations());

        serverable.setMapData(mapData);

    }

    //Called when ServerLoadEvent is listened for in SGLoadListener
    public void mapCallback() {
        List<Profile> allPlayers = new ArrayList<>(serverable.getPlayerList());

        List<Integer> spawnIndexes = MathUtil.distributeObjects(24, allPlayers.size());
        List<MapLocation> spawnLocations = serverable.getMapData().getSpawnLocations();
        Location centerLocation = serverable.getMapData().getCenterLocation().toBukkit(serverable.getWorld()).add(0.5, 0.5, 0.5);

        for (int i = 0; i < allPlayers.size(); i++) {
            int spawnIndex = spawnIndexes.get(i % spawnIndexes.size());

            Location worldLocation = MathUtil.lookAt(spawnLocations.get(spawnIndex).toBukkit(serverable.getWorld()), centerLocation);

            Profile profile = allPlayers.get(i);
            Player player = profile.getPlayer();
            SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

            player.teleport(worldLocation);
            temporaryData.setPedistalLocation(worldLocation);
            temporaryData.setDistrictId((i % 12) + 1);
            serverable.getTributeList().add(profile);
            PlayerUtil.refresh(profile);
        }

        MapData mapData = serverable.getMapData();

        serverable.announce(String.format("&eMap name&8: &2%s", mapData.getMapName()));
        serverable.announce(String.format("&eMap author&8: &2%s", mapData.getMapAuthor()));
        serverable.announce(String.format("&eMap link&8: &2%s", mapData.getMapLink()));
        serverable.announceTitle("&2" + mapData.getMapName(), "&6by " + mapData.getMapAuthor(), 5, 50, 10);

    }

    public void run() {
        int currentTime = gameTimer.decrement();
        //Switch to livegame
        if (currentTime == 0) {
            cancel();
            return;
        }

        if (currentTime == gameTimer.getInitialTime()) {

            Pair<String, String> sigUnit = gameTimer.toSignificantUnit();
            serverable.announce(String.format("&cPlease wait &8[&e%s&8] &c%s before the games begin.",
                    sigUnit.getKey(),
                    sigUnit.getValue()));
        }

        if (currentTime <= 10)
            serverable.announceTitle("", "&6" + currentTime , 2, 16, 2);

        if (currentTime <= 5 || currentTime == 10 || currentTime == 30) {
            Pair<String, String> significantUnit = gameTimer.toSignificantUnit();
            serverable.announce(String.format("&8[&e%s&8] &c%s until the games begin!",
                    significantUnit.getKey(),
                    significantUnit.getValue()));
        }
    }

    public void cancel() {
        super.cancel();

        serverable.setGameState(GameState.LIVEGAME);
    }

}
