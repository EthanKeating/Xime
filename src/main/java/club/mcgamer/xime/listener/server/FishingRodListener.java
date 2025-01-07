package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

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

        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();

            FishHook entity = (FishHook) event.getEntity();
            EntityFishingHook nmsEntity = (EntityFishingHook) ((CraftEntity) entity).getHandle();

            //nmsEntity.

        }
    }

}
