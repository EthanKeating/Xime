package club.mcgamer.xime.bg.design.bossbar;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.runnable.GameRunnable;
import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.lang.impl.Language;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.EndGameRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;


public class BGBossbarAdapter extends BossbarAdapter {

    @Override
    public void tick() {
    }

    @Override
    public String getTitle(Profile profile) {
        Language language = profile.getLanguage();

        if (!(profile.getServerable() instanceof BGServerable serverable)) return "";

        return "&3&lBattlegrounds";
    }

    @Override
    public float getHealth(Profile profile) {
        BGServerable serverable = (BGServerable) profile.getServerable();
        GameTimer gameTimer = serverable.getGameTimer();

        if (gameTimer == null)
            return 300.0f;

        return ((float) gameTimer.getCurrentTime() / (float) gameTimer.getInitialTime()) * 300.0f;
    }

    @Override
    public boolean isHidden(Profile profile) {
        return false;
    }
}
