package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.server.event.ServerQuitEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGQuitListener extends IListener {

    @EventHandler
    private void onSGJoin(ServerQuitEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            GameState gameState = serverable.getGameState();
            serverable.announceRaw(String.format("&2%s &6has left", profile.getDisplayName()));

            switch (gameState) {
                case LOBBY:
                    break;
                case PREGAME:
                case LIVEGAME:
                case PREDEATHMATCH:
                case DEATHMATCH:
                    if (serverable.getTributeList().contains(profile))
                        player.setHealth(0.0);

                    serverable.getTributeList().remove(profile);
                    serverable.getSpectatorList().remove(profile);
                    break;
                case CLEANUP:
                case RESTARTING:
                    //disallow
                    break;

            }


        }
    }
}
