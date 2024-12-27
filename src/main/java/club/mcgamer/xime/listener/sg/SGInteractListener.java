package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerAirInteractEvent;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.MathUtil;
import com.github.retrooper.packetevents.protocol.teleport.RelativeFlag;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class SGInteractListener extends IListener {

    @EventHandler
    private void onSGInteract(ServerAirInteractEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            if (serverable.getSpectatorList().contains(profile))
                switch (serverable.getGameState()) {
                    case LIVEGAME:
                    case PREDEATHMATCH:
                    case DEATHMATCH:
                        player.performCommand("spectate");
                }
        }
    }
}
