package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.animation.TextShineAnimation;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.runnable.PreGameRunnable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGJoinListener extends IListener {

    @EventHandler
    private void onSGJoin(ServerJoinEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            String prefix = serverable.getPrefix();

            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            PlayerData playerData = profile.getPlayerData();

            GameState gameState = serverable.getGameState();
            GameSettings gameSettings = serverable.getGameSettings();

            if (!gameSettings.isSilentJoinLeave() && !playerData.isSilentJoin())
                serverable.announceRaw(String.format("&2%s &6has joined&8.", profile.getDisplayName()));

            if(serverable instanceof SGMakerServerable)
                new TextShineAnimation(plugin, profile, "You joined " + ((SGMakerServerable) serverable).getOwner().getNameBypassDisguise() + "'s Custom Game " + serverable.getServerId());
            else
                new TextShineAnimation(plugin, profile, "You joined Survival Games " + serverable.getServerId());
            //profile.sendAction("&6&lYou joined Survival Games " + serverable.getServerId());

            switch (gameState) {
                case LOBBY:
                    profile.sendMessage(prefix + "&6&lAbout this lobby");
                    profile.sendMessage(prefix + "&fUsed in: &a" + serverable.getMapData().getMapLink());
                    profile.sendMessage(prefix + "&fCreated by: &a"  + serverable.getMapData().getMapAuthor());

                    player.setExp(0);
                    if (player.getGameMode() != GameMode.SURVIVAL)
                        player.setGameMode(GameMode.SURVIVAL);

                    PlayerUtil.refresh(profile);
                    if (serverable instanceof SGMakerServerable)
                        player.setLevel(0);
                    else
                        player.setLevel(serverable.getServerId());

                    player.teleport(serverable.getLobbyLocation());

                    if(profile.getPlayerData().isCanFly()) {
                        player.setAllowFlight(true);
                        PlayerUtil.setFlying(profile);
                    }

                    if (!(serverable instanceof SGMakerServerable)) {
                        player.performCommand("vote");

//                        if (serverable.getCurrentRunnable() instanceof LobbyRunnable lobbyRunnable) {
//                            if (serverable.get)
//                        }
                    }
                    break;
                case PREGAME:
                    if (!(serverable instanceof SGMakerServerable)) {
                        if (!serverable.isFull() && serverable.isJoinable()) {
                            if (serverable.getCurrentRunnable() instanceof PreGameRunnable preGameRunnable) {

                                if (!preGameRunnable.getUnusedSpawnIndexes().isEmpty()) {
                                    int goodId = preGameRunnable.getUnusedSpawnIndexes().get(0);

                                    preGameRunnable.prepPlayer(profile, goodId, goodId);
                                }
                            }
                        }
                        break;
                    }
                case LIVEGAME:
                case PREDEATHMATCH:
                case DEATHMATCH:
                case ENDGAME:
                case CLEANUP:

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        MapData mapData = serverable.getMapData();
                        if (mapData != null) {
                            serverable.setSpectating(profile);
                            Location specLocation = mapData.getSpectateLocation().toBukkit(serverable.getWorld());
                            player.teleport(specLocation);
                        }
                    }, 1L);
                    break;
                case RESTARTING:
                    //disallow
                    break;

            }


        }
    }
}
