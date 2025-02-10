package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.UUID;

public class LaunchPadListener extends IListener {

    private final HashSet<UUID> launchPaddingPlayers = new HashSet<>();

    @EventHandler
    private void onFallDamage(ServerDamageEvent event) {
        if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.FALL) {
            UUID uuid = event.getVictim().getUuid();

            if (launchPaddingPlayers.contains(uuid)) {
                event.getEvent().setCancelled(true);
                launchPaddingPlayers.remove(uuid);
            }
        }
    }

    @EventHandler
    public void onPressurePlateStep(ServerInteractEvent event) {

        Profile profile = event.getProfile();
        Player player = profile.getPlayer();

        if (event.getEvent().getAction() == Action.PHYSICAL) {

            Block clickedBlock = event.getEvent().getClickedBlock();

            if (clickedBlock != null && clickedBlock.getType() == Material.STONE_PLATE) {
                Block underBlock = clickedBlock.getRelative(BlockFace.DOWN);

                if (underBlock == null) return;

                switch (underBlock.getType()) {
                    case EMERALD_BLOCK:
                        launchPlayerForward(player, 0.0, 2.5);
                        break;
                    case REDSTONE_BLOCK:
                        launchPlayerForward(player, 3.5, 1.0);
                        break;
                }


            }

        }
    }

    private void launchPlayerForward(Player player, double forward, double upward) {
        launchPaddingPlayers.add(player.getUniqueId());

        double radYaw = Math.toRadians(player.getLocation().getYaw());
        player.setVelocity(new Vector(-Math.sin(radYaw) * forward, upward, Math.cos(radYaw) * forward));
    }

}
