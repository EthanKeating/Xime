package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.PreGameRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class BGLoadListener extends IListener {

    @EventHandler
    private void onSGWorldLoad(ServerLoadEvent event) {
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();

            serverable.start();
        }

    }

}
