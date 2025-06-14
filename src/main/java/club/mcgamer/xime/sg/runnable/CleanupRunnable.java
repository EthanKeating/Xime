package club.mcgamer.xime.sg.runnable;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;

public class CleanupRunnable extends AbstractGameRunnable {

    private final XimePlugin plugin;
    private final SGServerable serverable;
    private final GameTimer gameTimer;

    public CleanupRunnable(SGServerable serverable, XimePlugin plugin) {
        this.plugin = plugin;
        this.serverable = serverable;
        this.gameTimer = serverable.getGameTimer();

        gameTimer.setTime(4);
    }

    public void run() {
        int currentTime = gameTimer.decrement();

        if (currentTime == 2) {
            serverable.announce("&3The server is cleaning up. You will be returned to the lobby in a moment.");
            return;
        }

        if (currentTime == 0) {
            cancel();
            return;
        }
    }

    public void cancel() {
        super.cancel();

        serverable.setGameState(GameState.RESTARTING);
        serverable.reset();
        //Handle resetting server here
        //Maybe have a method in GameHandler that will reinit everything?
    }

}
