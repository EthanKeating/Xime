package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.MathUtil;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.*;
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
    public void onIncreasedRodSpeed(ProjectileLaunchEvent event) {
        if (event.getEntityType().equals(EntityType.FISHING_HOOK)) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player shooter = (Player) event.getEntity().getShooter();
                FishHook hook = (FishHook) event.getEntity();

                Profile profile = plugin.getProfileHandler().getProfile(shooter);

                if (profile == null) return;

                if (profile.getServerable() instanceof SGServerable) {
                    SGServerable serverable = (SGServerable) profile.getServerable();

                    hook.setVelocity(hook.getVelocity().multiply(serverable.getGameSettings().getRodSpeedMultiplier()));
                }
            }
        }
    }

//    @EventHandler
//    public void onIncreaseRodSpeed(ProjectileLaunchEvent event) {
//
//        if (!event.getEntityType().equals(EntityType.FISHING_HOOK)) return;
//
//        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.23));
//
//        new BukkitRunnable() {
//
//            int tick = 0;
//
//            @Override
//            public void run() {
//
//                if (event.getEntity() == null || event.getEntity().isDead() || event.getEntity().isOnGround())
//                    cancel();
//
//                tick++;
//
//                double slowdown = Math.min(tick, 5) * 0.016;
//
//                event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.0 - slowdown));
//            }
//        }.runTaskTimer(plugin, 1L, 1L);
//
//        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
//        }, 1L, 1L);
//    }



    @EventHandler
    public void onIncreaseRodSpeed(ProjectileLaunchEvent event) {

//        if (!event.getEntityType().equals(EntityType.FISHING_HOOK)) return;
//
//        Player player = (Player) event.getEntity().getShooter();
//
//        if (!player.getName().equalsIgnoreCase("Eths"))
//            return;
//        //test.getWorld().playEffect(test, Effect.HAPPY_VILLAGER, 1);
//        float baseDelta = 1.55f;
//
//        Location test = player.getEyeLocation();
//
//        test = test.add(test.getDirection().add(test.getDirection().normalize().multiply(baseDelta)));
//        event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(0.91f));
//        baseDelta *= 0.91f;
//
//        event.getEntity().teleport(test.add(0.0, -0.8, 0.0));
//
//
//        //Teleport fishing rod;
//
//        int tickSkips = 4;

//        while(--tickSkips > 0) {
//            motion *= 0.91f;
//
//            event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(0.91f));
//        }
//
//        event.getEntity().getLocation().getDirection();
//
//        new BukkitRunnable() {
//
//            int tick = 0;
//            Location originaLocation = event.getEntity().getLocation();
//
//            EntityFishingHook
//
//            @Override
//            public void run() {
//
//                if (event.getEntity() == null || event.getEntity().isDead() || event.getEntity().isOnGround())
//                    cancel();
//
//                tick++;
//
//                Location newLocation = event.getEntity().getLocation();
//
//                Bukkit.broadcastMessage(tick + ": xz:" + MathUtil.calculateXZDistance(originaLocation, newLocation) + ", y:" + (originaLocation.getY() - newLocation.getY()));
//                originaLocation = newLocation;
//            }
//        }.runTaskTimer(plugin, 1L, 1L);
//
//        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
//        }, 1L, 1L);
    }

}
