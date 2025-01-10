package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FishingRodListener extends IListener {

    @EventHandler
    public void onIncreaseRodSize(ProjectileLaunchEvent event) {
        if (event.getEntityType().equals(EntityType.FISHING_HOOK)) {
            Entity hook = (((CraftEntity) event.getEntity()).getHandle());

            hook.setSize(0.35f, 0.35f);

        }
    }

    @EventHandler
    public void onIncreaseRodSpeed(ProjectileLaunchEvent event) {

        if (!event.getEntityType().equals(EntityType.FISHING_HOOK)) return;

        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.23));

        new BukkitRunnable() {

            int tick = 0;

            @Override
            public void run() {

                if (event.getEntity() == null || event.getEntity().isDead() || event.getEntity().isOnGround())
                    cancel();

                tick++;

                double slowdown = Math.min(tick, 5) * 0.016;

                event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.0 - slowdown));
            }
        }.runTaskTimer(plugin, 1L, 1L);

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
        }, 1L, 1L);
    }

}
