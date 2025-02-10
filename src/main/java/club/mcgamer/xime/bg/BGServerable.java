package club.mcgamer.xime.bg;

import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.bg.design.bossbar.BGBossbarAdapter;
import club.mcgamer.xime.bg.design.sidebar.BGSidebarAdapter;
import club.mcgamer.xime.bg.leaderboard.LeaderboardEntry;
import club.mcgamer.xime.bg.menu.LoadoutMenu;
import club.mcgamer.xime.bg.runnable.GameRunnable;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.sg.design.sidebar.SGSidebarAdapter;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.MathUtil;
import club.mcgamer.xime.util.PlayerUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class BGServerable extends Serverable {

    private final int gameLength = 60 * 10; //10mins

    private String currentMapId;

    private GameRunnable gameRunnable;
    private GameTimer gameTimer = new GameTimer();

    private List<LeaderboardEntry> sortedLeaderboard = new ArrayList<>();

    private HashMap<String, Location> spawnLocations = new HashMap<>();
    private LoadoutMenu loadoutMenu = new LoadoutMenu(this);

    private HashMap<MapLocation, Long> placedBlocks = new HashMap<>();

    public BGServerable() {
        super();
        setMaxPlayers(32);

        setup();
    }

    @Override
    public TemporaryData createTemporaryData() {
        return new BGTemporaryData();
    }

    public String getPrefix() {
        return "&8[&3Battlegrounds&8] &f";
    }

    //Called on creation & on game end
    public void setup() {
        List<String> mapPool = new ArrayList<>(plugin.getMapHandler().getBgMapPool().keySet());
        Collections.shuffle(mapPool);

        currentMapId = mapPool.get(0); //randomly select this, or intead of random have it ascending

        spawnLocations = new HashMap<>();
        sortedLeaderboard = new ArrayList<>();

        setBossbarAdapter(new BGBossbarAdapter());
        setSidebarAdapter(new BGSidebarAdapter());
        setWorld(toString(), currentMapId);
        setJoinable(false);
    }

    public void announce(String message) {
        getPlayerList().forEach(profile -> profile.sendMessage("&8[&3Battlegrounds&8] &f" + message));
    }

    public void updateLeaderboard() {
        List<LeaderboardEntry> newSortedLeaderboard = new ArrayList<>();

        getPlayerList().forEach(loopProfile -> newSortedLeaderboard.add(new LeaderboardEntry(loopProfile, loopProfile.getRank().getColor() + loopProfile.getName(), loopProfile.getName(), ((BGTemporaryData) loopProfile.getTemporaryData()).getKills())));


        sortedLeaderboard = newSortedLeaderboard.stream()
                .sorted(Comparator.comparingInt(LeaderboardEntry::getKills).reversed())
                .collect(Collectors.toList());
    }

    //Called on Map Callback from BGLoadListener
    public void start() {

        this.setMapData(MapData.load(currentMapId));
        this.populateLocations();

        this.gameTimer.setTime(gameLength);

        this.setJoinable(true);
        this.gameRunnable = new GameRunnable(this);
        this.gameRunnable.runTaskTimer(plugin, 0, 20);
    }

    //Called on gameRunnable end or server stop
    public void reset() {

        if (this.gameRunnable != null)
            this.gameRunnable.cancel();

        getPlayerList().stream().forEach(profile -> {
            if (profile.getTemporaryData() instanceof BGTemporaryData temporaryData)
                temporaryData.setKills(0);

            Player player = profile.getPlayer();
            PlayerUtil.refresh(profile);
            PlayerUtil.setFlying(profile);
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        });
        setJoinable(false);
        setMapData(null);
        plugin.getWorldHandler().unload(getWorld(), false);
        setup();
    }

    public void stop() {
        super.stop();

        if (this.gameRunnable != null)
            this.gameRunnable.cancel();
    }

    public void populateLocations() {
        List<MapLocation> directionalLocations = getMapData().getSpawnLocations();

        MapLocation westLocation = directionalLocations.stream()
                .min((v1, v2) -> Double.compare(v1.getX(), v2.getX())).orElse(null);
        directionalLocations.remove(westLocation);

        MapLocation eastLocation = directionalLocations.stream()
                .max((v1, v2) -> Double.compare(v1.getX(), v2.getX())).orElse(null);
        directionalLocations.remove(eastLocation);

        MapLocation northLocation = directionalLocations.stream()
                .min((v1, v2) -> Double.compare(v1.getZ(), v2.getZ())).orElse(null);
        directionalLocations.remove(northLocation);

        MapLocation southLocation = directionalLocations.stream()
                .max((v1, v2) -> Double.compare(v1.getZ(), v2.getZ())).orElse(null);
        directionalLocations.remove(southLocation);

        Location centerLocation = directionalLocations.get(0).toBukkit(getWorld());
        centerLocation.setYaw(0);
        spawnLocations.put("center", centerLocation);

        Location clone = centerLocation.clone().add(0.5, 0.5, 0.5);

        spawnLocations.put("east", MathUtil.lookAt(eastLocation.toBukkit(getWorld()), clone));
        spawnLocations.put("west", MathUtil.lookAt(westLocation.toBukkit(getWorld()), clone));
        spawnLocations.put("north", MathUtil.lookAt(northLocation.toBukkit(getWorld()), clone));
        spawnLocations.put("south", MathUtil.lookAt(southLocation.toBukkit(getWorld()), clone));
    }

    public void setWaiting(Profile profile) {
        BGTemporaryData temporaryData = (BGTemporaryData) profile.getTemporaryData();
        Serverable serverable = profile.getServerable();
        Player player = profile.getPlayer();

        temporaryData.setWaiting(true);
        PlayerUtil.refresh(profile);
        if (player.getGameMode() != GameMode.SURVIVAL)
            player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().setItem(4, new ItemBuilder(Material.BOOK).name("&6Loadout Editor").build());

        player.teleport(getMapData() == null || getWorld() == null ? Bukkit.getWorlds().get(0).getSpawnLocation()
                : MathUtil.lookAt(getMapData().getCenterLocation().toBukkit(getWorld()), getMapData().getSpectateLocation().toBukkit(getWorld()).add(0.5, 0.5, 0.5)));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (profile.getServerable() == serverable) {
                player.getInventory().setItem(4, new ItemBuilder(Material.BOOK).name("&6Loadout Editor").build());
            }

        }, 1L);
    }

    public void setFighting(Profile profile, String direction) {
        if (direction == null)
            direction = "center";

        Player player = profile.getPlayer();
        BGTemporaryData temporaryData = (BGTemporaryData) profile.getTemporaryData();

        PlayerUtil.refresh(profile);
        if (player.getGameMode() != GameMode.SURVIVAL)
            player.setGameMode(GameMode.SURVIVAL);

        player.teleport(getSpawnLocations().get(direction));
        temporaryData.setWaiting(false);
        PlayerData playerData = profile.getPlayerData();

        int swordSlot = playerData.getBgSwordSlot();
        int rodSlot = playerData.getBgRodSlot();
        int bowSlot = playerData.getBgBowSlot();
        int fnsSlot = playerData.getBgFNSSlot();
        int gapSlot = playerData.getBgGapSlot();
        int arrowSlot = playerData.getBgArrowSlot();

        player.getInventory().setItem(swordSlot, new ItemBuilder(Material.IRON_SWORD).unbreakable().build());
        player.getInventory().setItem(rodSlot, new ItemBuilder(Material.FISHING_ROD).unbreakable().build());
        player.getInventory().setItem(bowSlot, new ItemBuilder(Material.BOW).unbreakable().build());
        player.getInventory().setItem(fnsSlot, new ItemBuilder(Material.FLINT_AND_STEEL).build());
        player.getInventory().setItem(gapSlot, new ItemBuilder(Material.GOLDEN_APPLE).amount(2).build());
        player.getInventory().setItem(arrowSlot, new ItemBuilder(Material.ARROW).amount(16).build());

        player.getInventory().setHelmet(new ItemBuilder(Material.IRON_HELMET).unbreakable().build());
        player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).unbreakable().build());
        player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).unbreakable().build());
        player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).unbreakable().build());


    }

}
