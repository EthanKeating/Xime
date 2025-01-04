package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.profile.Profile;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class SGMoveListener extends IListener {

    @EventHandler
    private void onSGMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof SGServerable) {

            SGServerable serverable = (SGServerable) profile.getServerable();
            SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

            if (serverable.getGameState() == GameState.PREGAME || serverable.getGameState() == GameState.PREDEATHMATCH) {
                if (temporaryData == null || temporaryData.getPedistalLocation() == null)
                    return;

                Location pedistalLocation = temporaryData.getPedistalLocation();
                Location playerLocation = event.getTo();

                double distance = MathUtil.calculateXZDistance(pedistalLocation, playerLocation);

                if (distance > 0.03) {
                    Location newLocation = new Location(
                            pedistalLocation.getWorld(),
                            pedistalLocation.getX(), pedistalLocation.getY(),
                            pedistalLocation.getZ(),
                            event.getTo().getYaw(),
                            event.getTo().getPitch()
                    );

                    player.teleport(newLocation);
                    profile.getUser().sendPacket(new WrapperPlayServerEntityVelocity(player.getEntityId(), new Vector3d(0.0, 0.0, 0.0)));
                    profile.getUser().sendPacket(new WrapperPlayServerPlayerPositionAndLook(
                            new Vector3d(pedistalLocation.getX(),
                                pedistalLocation.getY(),
                                pedistalLocation.getZ()),
                            0f,
                            0f,
                            (byte) (RelativeFlag.YAW.getMask() | RelativeFlag.PITCH.getMask()),
                           -1));
                    profile.getUser().flushPackets();
                }
            }

        }
    }
}
