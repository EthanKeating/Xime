package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.map.impl.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.MathUtil;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.PlayerUtil;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PreGameRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    @Getter private List<Integer> unusedSpawnIndexes = new ArrayList<>();
    private List<Integer> spawnIndexes = new ArrayList<>();
    private List<MapLocation> spawnLocations;
    private Location centerLocation;

    private boolean mapLoaded;

    public PreGameRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getPreGameLength());
        serverable.setJoinable(false);

        VoteableMap mapWinner = serverable.getMapPool().complete();
        MapData mapData = mapWinner.getMapData();
        serverable.getPreviousMapNames().add(mapData.getMapName());

        serverable.setMapWinner(mapWinner);
        serverable.setMapData(mapData);

        serverable.setWorld(serverable.toString(), mapWinner.getMapIdentifier());
        if (mapData.getDmCenterLocation() == null)
            mapData.setDmCenterLocation(mapData.getSpectateLocation() == null ? mapData.getCenterLocation() : mapData.getSpectateLocation());
        if (mapData.getDmLocations().isEmpty())
            mapData.getDmLocations().addAll(mapData.getSpawnLocations());
        if (mapData.getSpectateLocation() == null)
            mapData.setSpectateLocation(mapData.getCenterLocation());

        serverable.setMapData(mapData);

        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();
        if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
            for(SGTeam team : teamProvider.getTeams()) {
                team.setOriginalPlayers();
            }
            teamProvider.removeEmpty();
        }

    }

    //Called when ServerLoadEvent is listened for in SGLoadListener
    public void mapCallback() {

        List<Profile> allPlayers = new ArrayList<>();
        List<Profile> spectators = new ArrayList<>();

//        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();
//        if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
//            for(SGTeam team : teamProvider.getTeams()) {
//                team.setOriginalPlayers();
//                allPlayers.addAll(team.getOriginalPlayers());
//            }
//        } else {
//            allPlayers.addAll(serverable.getPlayerList());
//        }

        centerLocation = serverable.getMapData().getCenterLocation().toBukkit(serverable.getWorld()).add(0.5, 0.5, 0.5);



        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();
        if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
            for(Profile profile : serverable.getPlayerList()) {
                if (teamProvider.hasTeam(profile)) {
                    allPlayers.add(profile);
                }
                else {
                    profile.sendMessage("&7You did not join a team and have become a spectator");
                    spectators.add(profile);
                }

            }
        } else {
            allPlayers.addAll(serverable.getPlayerList());
        }

        spawnLocations = serverable.getMapData().getSpawnLocations();
        unusedSpawnIndexes = IntStream.range(0, spawnLocations.size()).boxed().collect(Collectors.toList());
        spawnIndexes = MathUtil.distributeObjects(spawnLocations.size(), allPlayers.size());

        serverable.getWorld().setGameRuleValue("doDaylightCycle", String.valueOf(serverable.getGameSettings().isDayLightCycle()));
        serverable.getWorld().setGameRuleValue("naturalRegeneration", String.valueOf(serverable.getGameSettings().isNaturalRegeneration()));

        for (int i = 0; i < allPlayers.size(); i++) {
            int spawnIndex = spawnIndexes.get(i % spawnIndexes.size());

            Profile profile = allPlayers.get(i);
            prepPlayer(profile, spawnIndex, i);
        }

        //TODO: Make pregame joinable
        serverable.setJoinable(true);
        mapLoaded = true;

        for(Profile profile : spectators) {
            serverable.setSpectating(profile);
            profile.getPlayer().teleport(centerLocation);
        }
    }

    public void prepPlayer(Profile profile, int spawnIndex, int index) {

        Location worldLocation = MathUtil.lookAt(spawnLocations.get(spawnIndex).toBukkit(serverable.getWorld()), centerLocation);

        Player player = profile.getPlayer();
        SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

        unusedSpawnIndexes.removeAll(Arrays.asList(spawnIndex));

        if (serverable.getGameSettings().isRandomizeNames())
            plugin.getDisguiseHandler().disguise(profile);

        player.teleport(worldLocation);
        temporaryData.setPedistalLocation(worldLocation);
        temporaryData.setDistrictId((index % 12) + 1);
        temporaryData.setLifeStart(System.currentTimeMillis());
        serverable.getTributeList().add(profile);
        PlayerUtil.refresh(profile);

        if (serverable.getGameSettings().isNoHitDelay())
            player.setMaximumNoDamageTicks(3);

        player.setMaxHealth(serverable.getGameSettings().getMaxHealth());
        player.setHealth(serverable.getGameSettings().getMaxHealth());
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void run() {
        int currentTime = gameTimer.decrement();

        if (!mapLoaded) {
            gameTimer.reset();
            return;
        }

        //Switch to livegame
        if (currentTime == 0) {
            cancel();
            return;
        }

        if (currentTime == gameTimer.getInitialTime()) {

            MapData mapData = serverable.getMapData();
            serverable.announceTitle("&2" + mapData.getMapName(), "&6by " + mapData.getMapAuthor(), 5, 50, 10);


            serverable.announce(String.format("&eMap name&8: &2%s", mapData.getMapName()));
            serverable.announce(String.format("&eMap author&8: &2%s", mapData.getMapAuthor()));
            serverable.announce(String.format("&eMap link&8: &2%s", mapData.getMapLink()));

            Pair<String, String> sigUnit = gameTimer.toSignificantUnit();
            serverable.announce(String.format("&cPlease wait &8[&e%s&8] &c%s before the games begin.",
                    sigUnit.getKey(),
                    sigUnit.getValue()));
        }

        if (currentTime <= 10)
            serverable.announceTitle("&6" + currentTime, "", 0, 30, 0);

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
