package club.mcgamer.xime.sgmaker.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.impl.MapPool;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter @Setter
public class MakerLobbyRunnable extends LobbyRunnable {

    private boolean startGame = false;

    public MakerLobbyRunnable(SGMakerServerable serverable, XimePlugin plugin) {
        super(serverable, plugin, serverable.getGameSettings());

        //serverable.getMapPool().setMap(MapPool.getMapIdentifiers().get(0));
        serverable.getGameTimer().setTime(4);

        //Initialize all data
    }

    @Override
    public void run() {
        if (!startGame) {
            gameTimer.reset();
            return;
        }

        if (serverable.getPlayerList().isEmpty()) {
            serverable.announce("&cUnable to start the game because there are no players");
            gameTimer.reset();
            startGame = false;
            return;
        }

        if (serverable.getGameSettings().getTeamProvider().getTeamType() != TeamType.NO_TEAMS
                && serverable.getGameSettings().getTeamProvider().getTeams().stream()
                .mapToInt(team -> team.getPlayers().size())
                .sum() == 0) {
            serverable.announce("&cUnable to start the game because there are no players");
            gameTimer.reset();
            startGame = false;
            return;
        }

        int currentTime = gameTimer.decrement();

        if (currentTime <= 5 || currentTime == 10 || currentTime % 30 == 0) {
            Pair<String, String> significantUnit = gameTimer.toSignificantUnit();

            serverable.announce(String.format("&8[&e%s&8] &c%s until lobby ends!", significantUnit.getKey(), significantUnit.getValue()));
        }

        if (currentTime == 1) {
            Bukkit.getScheduler().runTaskLater(plugin, this::cancel, 11L);
        }
    }


}
