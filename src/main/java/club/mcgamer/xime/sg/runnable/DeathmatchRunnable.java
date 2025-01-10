package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapLocation;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.util.Pair;
import lombok.Getter;
import org.bukkit.Location;

import java.util.concurrent.CopyOnWriteArrayList;

public class DeathmatchRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;
    private final GameSettings gameSettings;

    private final MapLocation centerLocation;
    @Getter private double borderSize;
    private double decrement;

    public DeathmatchRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();
        this.gameSettings = serverable.getGameSettings();

        gameTimer.setTime(gameSettings.getDeathmatchTime());

        centerLocation = serverable.getMapData().getDmCenterLocation();
        MapLocation firstSpawnLocation = serverable.getMapData().getDmLocations().get(0);

        borderSize = Math.sqrt(
                Math.pow(centerLocation.getX() - firstSpawnLocation.getX(), 2) +
                        Math.pow(centerLocation.getZ() - firstSpawnLocation.getZ(), 2)) * 1.5;

        decrement = (borderSize - 5) / gameSettings.getDeathmatchShrinkTime();
        //Initialize all data
    }

    public void run() {
        int currentTime = gameTimer.decrement();

        Pair<String, String> sigUnit = gameTimer.toSignificantUnit();

        int seconds = gameTimer.getSeconds();
        int minutes = gameTimer.getMinutes();

        if (currentTime == 0) {
            cancel();
            return;
        }

        //&aThe games have ended!
        if (currentTime <= gameSettings.getDeathmatchShrinkTime())
            borderSize = Math.max(borderSize - decrement, 0);
        if (currentTime % 5 == 0) {
            for (Profile loopProfile : new CopyOnWriteArrayList<>(serverable.getTributeList())) {
                Location playerLocation = loopProfile.getPlayer().getLocation().add(0, -2, 0);
                MapLocation centerLocation = serverable.getMapData().getCenterLocation();

                double distance = Math.sqrt(
                        Math.pow(playerLocation.getX() - centerLocation.getX(), 2) +
                                Math.pow(playerLocation.getZ() - centerLocation.getZ(), 2));

                if (distance > borderSize) {
                    playerLocation.getWorld().strikeLightningEffect(playerLocation);
                    if (loopProfile.getPlayer().getHealth() < 6.0)
                        loopProfile.getPlayer().setHealth(0.0);
                    else
                        loopProfile.getPlayer().damage(6.0);
                    loopProfile.sendMessage("&8[&6MCSG&8] &4Please return to spawn!");
                    //loopProfile.sendTitle("", "&cYou must stay in the deathmatch area!", 0, 30, 10);
                }
            }
        }

        if ((seconds == 0 && minutes > 0) || (minutes == 0 && (seconds == 30 || seconds == 10 || seconds <= 5) )) {
            serverable.announce(String.format("&8[&e%s&8] &c%s until the deathmatch ends!", sigUnit.getKey(), sigUnit.getValue()));
        }

        if (currentTime == gameTimer.getInitialTime()) {
            serverable.announce("&cFight to the death!");
        }
    }

    public void cancel() {
        super.cancel();

        serverable.setGameState(GameState.CLEANUP);
    }

}
