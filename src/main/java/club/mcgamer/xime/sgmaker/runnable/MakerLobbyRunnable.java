package club.mcgamer.xime.sgmaker.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Getter @Setter
public class MakerLobbyRunnable extends LobbyRunnable {

    private boolean startGame = false;

    public MakerLobbyRunnable(SGMakerServerable serverable, XimePlugin plugin) {
        super(serverable, plugin);

        serverable.getMapPool().setMap(MapPool.getMapIdentifiers().get(0));
        serverable.getGameTimer().setTime(4);

        //Initialize all data
    }

    @Override
    public void run() {
        if (!startGame) {
            gameTimer.reset();
            return;
        }

        int currentTime = gameTimer.decrement();

        if (currentTime == 0) {
            cancel();
            return;
        }

        if (currentTime <= 5 || currentTime == 10 || currentTime % 30 == 0) {
            Pair<String, String> significantUnit = gameTimer.toSignificantUnit();

            serverable.announce(String.format("&8[&e%s&8] &c%s until lobby ends!", significantUnit.getKey(), significantUnit.getValue()));
        }
    }


}
