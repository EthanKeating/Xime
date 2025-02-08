package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerQuitEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.DisguiseUtil;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SGQuitListener extends IListener {

    @EventHandler
    private void onSGJoin(ServerQuitEvent event) {
        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            PlayerData playerData = profile.getPlayerData();

            GameState gameState = serverable.getGameState();
            GameSettings gameSettings = serverable.getGameSettings();

            if (!gameSettings.isSilentJoinLeave() && !playerData.isSilentJoin())
                serverable.announceRaw(String.format("&2%s &6has left&8.", profile.getDisplayName()));

            if (gameSettings.isRandomizeNames())
                plugin.getDisguiseHandler().undisguiseNoRefresh(profile);

            SGTeamProvider teamProvider = gameSettings.getTeamProvider();

            switch (gameState) {
                case LOBBY:
                    if (teamProvider.getTeamType() != TeamType.NO_TEAMS) {
                        SGTeam team = teamProvider.getTeam(profile);

                        if (team != null)
                            team.removePlayer(profile);
                    }

                    if (serverable.getMapPool() != null)
                        serverable.getMapPool().removeVote(profile);
                    break;
                case PREGAME:
                case LIVEGAME:
                case PREDEATHMATCH:
                case DEATHMATCH:
                case ENDGAME:
                case CLEANUP:
                case RESTARTING:
                    if (serverable.getTributeList().contains(profile))
                        player.setHealth(0.0);

                    serverable.getTributeList().remove(profile);
                    serverable.getSpectatorList().remove(profile);
                    break;

            }


        }
    }
}
