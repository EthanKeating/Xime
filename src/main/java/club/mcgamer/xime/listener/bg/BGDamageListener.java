package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerDamageOtherEntityEvent;
import club.mcgamer.xime.server.event.ServerDeathEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.MathUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class BGDamageListener extends IListener {

    @EventHandler
    private void onBGDamage(ServerDamageEvent event) {
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();
            Profile victim = event.getVictim();

            if (!(victim.getTemporaryData() instanceof BGTemporaryData victimTemporaryData)) {
                event.getEvent().setCancelled(true);
                return;
            }

            if (victimTemporaryData.isWaiting()) {
                event.getEvent().setCancelled(true);
                return;
            }

            if (victim.getPlayer().isDead())
                event.getEvent().setCancelled(true);

            if (event.getAttacker().isPresent()) {
                Profile attacker = event.getAttacker().get();



                if (!(attacker.getTemporaryData() instanceof BGTemporaryData)) {
                    if (event.getEvent().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        event.getEvent().setCancelled(true);
                        return;
                    }
                }
                BGTemporaryData attackerTemporaryData = (BGTemporaryData) attacker.getTemporaryData();

                if (attackerTemporaryData.isWaiting() && event.getEvent().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.getEvent().setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    private void onCombust(EntityCombustEvent event) {
        if (event.getEntity() instanceof Player player) {
            Profile profile = plugin.getProfileHandler().getProfile(player);
            if (profile == null) return;

            if (profile.getTemporaryData() instanceof BGTemporaryData temporaryData)
                if (temporaryData.isWaiting())
                    event.setCancelled(true);
        }

    }

    @EventHandler
    private void onPainting(HangingBreakByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            Profile profile = plugin.getProfileHandler().getProfile(player);
            if (profile == null) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBGDamageOtherEntity(ServerDamageOtherEntityEvent event) {
        if (event.getServerable() instanceof BGServerable serverable) {
            event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onBGProjectileDamage(ServerDamageEvent event) {
        if (event.getServerable() instanceof BGServerable serverable) {
            if (!(event.getEvent() instanceof EntityDamageByEntityEvent entityDamageByEntityEvent)) return;
            if (event.getAttacker().isEmpty()) return;

            if (entityDamageByEntityEvent.getDamager() instanceof Arrow) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    double baseHealth = ((int) Math.round(event.getVictim().getPlayer().getHealth())) / 2.0;
                    NumberFormat healthFormat = new DecimalFormat("##.#");
                    String formattedHealth = healthFormat.format(Math.max(0.5, baseHealth));
                    String shotMessage = String.format("&8[&3Battlegrounds&8] %s &eis now at &8[&c%s❤&8]", event.getVictim().getDisplayName(), formattedHealth);
                    if (baseHealth < 10.0)
                        event.getAttacker().get().sendMessage(shotMessage);
                }, 1L);
            }
        }
    }

    @EventHandler
    private void onBGDeath(ServerDeathEvent event) {
        if (event.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) event.getServerable();
            Profile victim = event.getVictim();
            PlayerData victimData = victim.getPlayerData();

            event.getEvent().getDrops().clear();
            event.getEvent().setDroppedExp(0);

            generateFirework(victim.getPlayer().getLocation());
            victimData.setBgDeaths(victimData.getBgDeaths() + 1);

            if (event.getAttacker().isPresent()) {
                Profile attacker = event.getAttacker().get();

                if(!(attacker.getServerable() instanceof BGServerable) || attacker.getServerable() != victim.getServerable())
                    return;

                PlayerData attackerData = attacker.getPlayerData();
                attackerData.setBgKills(attackerData.getBgKills() + 1);

                if (event.getEvent().getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                    attackerData.setBgBowKills(attackerData.getBgBowKills() + 1);

                double baseHealth = ((int) Math.round(attacker.getPlayer().getHealth())) / 2.0;
                NumberFormat healthFormat = new DecimalFormat("##.#");
                String formattedHealth = healthFormat.format(Math.max(0.5, baseHealth));
                victim.sendMessage(String.format("&8[&3Battlegrounds&8] &eYou were killed by %s&e with &8[&c%s❤&8]",
                        attacker.getDisplayName(),
                        formattedHealth));
                attacker.sendMessage(String.format("&8[&3Battlegrounds&8] &eYou killed %s&e with &8[&c%s❤&8]",
                        victim.getDisplayName(),
                        formattedHealth));

                if (!((BGTemporaryData)attacker.getTemporaryData()).isWaiting()) {
                    int gappleCount = 0;
                    for (ItemStack item : attacker.getPlayer().getInventory().getContents()) {

                        if (item != null && item.getType() == Material.GOLDEN_APPLE)
                            gappleCount += item.getAmount();
                    }
                    if (gappleCount < 2) {
                        if (gappleCount == 0) {
                            int gapSlot = attacker.getPlayerData().getBgGapSlot();
                            ItemStack itemAtGap = attacker.getPlayer().getInventory().getItem(gapSlot);

                            if (itemAtGap == null || itemAtGap.getType() == Material.AIR) {
                                attacker.getPlayer().getInventory().setItem(gapSlot, new ItemStack(Material.GOLDEN_APPLE));
                            } else {
                                attacker.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                            }
                        } else {
                            attacker.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                        }
                    }
                    if (!attacker.getPlayer().getInventory().contains(Material.FLINT_AND_STEEL)) {
                        int fnsSlot = attacker.getPlayerData().getBgFNSSlot();
                        ItemStack itemAtFNS = attacker.getPlayer().getInventory().getItem(fnsSlot);

                        if (itemAtFNS == null || itemAtFNS.getType() == Material.AIR) {
                            attacker.getPlayer().getInventory().setItem(fnsSlot, new ItemStack(Material.FLINT_AND_STEEL));
                        } else {
                            attacker.getPlayer().getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
                        }
                    }
                    attacker.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 3 * 20, 3));
                }
                if (attacker.getTemporaryData() instanceof BGTemporaryData attackerTemporaryData) {
                    attackerTemporaryData.setKills(attackerTemporaryData.getKills() + 1);
                }
            }

            serverable.setWaiting(victim);
            event.getVictim().getPlayer().setVelocity(new Vector(0.0, 0.0, 0.0));
        }
    }

    private void generateFirework(Location location) {
        Random random = new Random();

        Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.LIME, Color.AQUA};
        Color primaryColor = colors[random.nextInt(colors.length)];

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(primaryColor)
                .build();

        ItemStack fireworkItem = new ItemStack(Material.FIREWORK);
        FireworkMeta meta = (FireworkMeta) fireworkItem.getItemMeta();
        if (meta != null) {
            meta.addEffect(effect);
            meta.setPower(1);
            fireworkItem.setItemMeta(meta);
        }

        Firework firework = location.getWorld().spawn(location, Firework.class);
        firework.setFireworkMeta((FireworkMeta) fireworkItem.getItemMeta());
    }


}
