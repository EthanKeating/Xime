package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.PreGameRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class SGLoadListener extends IListener {

    @EventHandler
    private void onSGWorldLoad(ServerLoadEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();

            if (serverable.getGameState() == GameState.PREGAME) {
                if (serverable.getCurrentRunnable() instanceof PreGameRunnable) {
                    PreGameRunnable runnable = (PreGameRunnable) serverable.getCurrentRunnable();

                    runnable.mapCallback();
                }
            }
        }

    }
}
