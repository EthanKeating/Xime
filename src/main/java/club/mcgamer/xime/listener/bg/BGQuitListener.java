package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CombatTagData;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerQuitEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BGQuitListener extends IListener {

    @EventHandler
    private void onBGQuit(ServerQuitEvent event) {
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            BGTemporaryData temporaryData = (BGTemporaryData) profile.getTemporaryData();
            CombatTagData combatTagData = profile.getCombatTagData();
            PlayerData playerData = profile.getPlayerData();

            if (!playerData.isSilentJoin())
                serverable.announceRaw(String.format("&2%s &6has left&8.", profile.getDisplayName()));

            if (temporaryData.isWaiting() || !combatTagData.isActive())
                return;

            player.setHealth(0.0);
        }
    }
}
