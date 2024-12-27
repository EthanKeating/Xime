package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
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
            serverable.announceRaw(String.format("&2%s &6has joined", profile.getDisplayName()));

            switch (gameState) {
                case LOBBY:
                    if (serverable.getCurrentRunnable() instanceof LobbyRunnable) {
                        LobbyRunnable runnable = (LobbyRunnable) serverable.getCurrentRunnable();

                        profile.sendMessage(String.format("&8[&6MCSG&8] &2Players waiting&8: &8[&6%s&8/&6%s&8]. &2Game requires &8[&6%s&8] &2to play.",
                                serverable.getPlayerList().size(),
                                serverable.getMaxPlayers(),
                                serverable.getGameSettings().getMinimumPlayers()));
                        profile.sendMessage("&8[&6MCSG&8] &2Vote using &8[&a/vote #&8].");
                        runnable.sendVotes(profile);
                        PlayerUtil.refresh(profile);
                        player.setLevel(serverable.getServerId());
                        player.teleport(serverable.getLobbyLocation());
                    }
                    break;
                case PREGAME:
                    //Set them as living player
                    break;
                case LIVEGAME:
                case PREDEATHMATCH:
                case DEATHMATCH:
                    MapData mapData = serverable.getMapData();
                    if (mapData != null) {
                        Location centerLocation = mapData.getCenterLocation().toBukkit(serverable.getWorld());
                        player.teleport(centerLocation);
                    }
                    serverable.setSpectating(profile);
                    break;
                case CLEANUP:
                case RESTARTING:
                    //disallow
                    break;

            }


        }
    }
}
