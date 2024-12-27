package club.mcgamer.xime.sg.design.bossbar;

import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.CleanupRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;

import java.util.concurrent.atomic.AtomicReference;

public class SGBossbarAdapter extends BossbarAdapter {

    @Override
    public void tick() {
    }

    @Override
    public String getTitle(Profile profile) {
        SGServerable serverable = (SGServerable) profile.getServerable();
        if (serverable.getGameState() == GameState.CLEANUP) {
            if (serverable.getCurrentRunnable() == null || (!(serverable.getCurrentRunnable() instanceof CleanupRunnable)))
                return "";
            CleanupRunnable cleanupRunnable = (CleanupRunnable) serverable.getCurrentRunnable();

            if (cleanupRunnable.getGameWinner().isPresent()) {
                return String.format("&8[&6MCSG&8] &a%s &ahas won the Survival Games!", cleanupRunnable.getGameWinner().get().getDisplayName());
            }
            return "&8[&6MCSG&8] &aThe games have ended!";
        }


        return "";
    }

    @Override
    public float getHealth(Profile profile) {
        SGServerable serverable = (SGServerable) profile.getServerable();
        GameTimer gameTimer = serverable.getGameTimer();

        if (serverable.getCurrentRunnable() == null)
            return 300.0f;

        return ((float) gameTimer.getCurrentTime() / (float) gameTimer.getInitialTime()) * 300.0f;
    }

    @Override
    public boolean isHidden(Profile profile) {
        SGServerable serverable = (SGServerable) profile.getServerable();

        return serverable.getGameState() != GameState.CLEANUP;
    }
}
