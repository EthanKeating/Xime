package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGJoinListener extends IListener {

    @EventHandler
    private void onSGJoin(ServerJoinEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            GameState gameState = serverable.getGameState();
            GameSettings gameSettings = serverable.getGameSettings();

            if (!gameSettings.isSilentJoinLeave())
                serverable.announceRaw(String.format("&2%s &6has joined&8.", profile.getDisplayName()));

            switch (gameState) {
                case LOBBY:
                    if (serverable.getCurrentRunnable() instanceof LobbyRunnable) {
                        LobbyRunnable runnable = (LobbyRunnable) serverable.getCurrentRunnable();

                        player.performCommand("vote");
                        PlayerUtil.refresh(profile);
                        player.setLevel(serverable.getServerId());
                        player.teleport(serverable.getLobbyLocation());

                        if(profile.getGeneralData().isCanFly())
                            player.setAllowFlight(true);
                    }
                    break;
                case PREGAME:
                case LIVEGAME:
                case PREDEATHMATCH:
                case DEATHMATCH:
                case CLEANUP:
                    MapData mapData = serverable.getMapData();
                    if (mapData != null) {
                        Location centerLocation = mapData.getCenterLocation().toBukkit(serverable.getWorld());
                        player.teleport(centerLocation);
                    }
                    serverable.setSpectating(profile);
                    break;
                case RESTARTING:
                    //disallow
                    break;

            }


        }
    }
}
