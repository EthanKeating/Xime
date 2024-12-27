package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

public class CleanupRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    @Getter private final Optional<Profile> gameWinner;

    public CleanupRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getCleanupTime());

        gameWinner = serverable.getTributeList().stream()
                .filter(profile -> profile.getTemporaryData() instanceof SGTemporaryData)
                .max(Comparator.comparingInt(profile -> ((SGTemporaryData) profile.getTemporaryData()).getKillCount()));

        serverable.announce("&aThe games have ended!");
        gameWinner.ifPresent(profile -> serverable.announce(String.format("&a%s &ahas won the Survival Games!", profile.getDisplayName())));
        serverable.getWorld().setTime(18000);

        MapData mapData = serverable.getMapData();
        List<MapLocation> fireworkLocations = new ArrayList<>(mapData.getSpawnLocations());
        if (mapData.getDmLocations().get(0) != mapData.getSpawnLocations().get(0))
            fireworkLocations.addAll(mapData.getDmLocations());

        fireworkLocations.stream()
                .map(mapLocation -> mapLocation.toBukkit(serverable.getWorld()))
                .forEach(this::generateFirework);
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

        ServerHandler serverHandler = plugin.getServerHandler();

        Serverable fallBack = serverHandler.getFallback();

        serverable.setGameState(GameState.RESTARTING);
        serverable.reset();
        //Handle resetting server here
        //Maybe have a method in GameHandler that will reinit everything?
    }

    private void generateFirework(Location location) {
        Random random = new Random();

        Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.LIME, Color.AQUA};
        Color primaryColor = colors[random.nextInt(colors.length)];

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(primaryColor)
                .with(FireworkEffect.Type.BALL)
                .build();

        ItemStack fireworkItem = new ItemStack(Material.FIREWORK);
        FireworkMeta meta = (FireworkMeta) fireworkItem.getItemMeta();
        if (meta != null) {
            meta.addEffect(effect);
            meta.setPower(1);
            fireworkItem.setItemMeta(meta);
        }

        Firework firework = location.getWorld().spawn(location, Firework.class);
        firework.setFireworkMeta((FireworkMeta) fireworkItem.getItemMeta());
    }

}
