package club.mcgamer.xime.sg.design.bossbar;

import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.lang.impl.Language;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.EndGameRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;


public class SGBossbarAdapter extends BossbarAdapter {

    @Override
    public void tick() {
    }

    @Override
    public String getTitle(Profile profile) {
        Language language = profile.getLanguage();

        if (!(profile.getServerable() instanceof SGServerable serverable)) return "";

        String prefix = serverable.getPrefix();
        if (serverable.getGameState() == GameState.ENDGAME) {
            if (serverable.getCurrentRunnable() == null || (!(serverable.getCurrentRunnable() instanceof EndGameRunnable)))
                return "";
            EndGameRunnable endGameRunnable = (EndGameRunnable) serverable.getCurrentRunnable();

            if (endGameRunnable.getGameWinner().isPresent() && endGameRunnable.getGameWinner().get().getPlayer() != null) {
                return String.format(prefix + "&a%s &ahas won the Survival Games!", endGameRunnable.getGameWinner().get().getDisplayName());
            }
            if (endGameRunnable.getGameWinnerTeam().isPresent()) {
                return String.format(prefix + "&6Team #%s &ahas won the Survival Games!", endGameRunnable.getGameWinnerTeam().get().getTeamId());
            }
            return prefix + "&aThe games have ended!";
        }

        return language.getBossBarText();
    }

    @Override
    public float getHealth(Profile profile) {

        if (!(profile.getServerable() instanceof SGServerable serverable)) return 300.0f;

        GameTimer gameTimer = serverable.getGameTimer();

        if (serverable.getCurrentRunnable() == null)
            return 300.0f;

        return ((float) gameTimer.getCurrentTime() / (float) gameTimer.getInitialTime()) * 300.0f;
    }

    @Override
    public boolean isHidden(Profile profile) {
//        SGServerable serverable = (SGServerable) profile.getServerable();
//
//        return serverable.getGameState() != GameState.CLEANUP;
        return false;
    }
}
