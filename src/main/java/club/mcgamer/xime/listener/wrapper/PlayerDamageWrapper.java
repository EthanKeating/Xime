package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.profile.data.temporary.CombatTagData;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerDamageEvent;
import club.mcgamer.xime.server.event.ServerDamageOtherEntityEvent;
import club.mcgamer.xime.server.event.ServerDeathEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDamageWrapper extends IListener {

    @EventHandler
    private void onDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (event.getDamager().getType() != EntityType.PLAYER) return;

        ProfileHandler profileHandler = plugin.getProfileHandler();

        Profile victim = profileHandler.getProfile((Player) event.getEntity());
        Profile attacker = profileHandler.getProfile((Player) event.getDamager());

        Serverable serverable = victim.getServerable();
        CombatTagData combatTagData = victim.getCombatTagData();

        if (attacker.getServerable() != serverable) {
            event.setCancelled(true);
            return;
        }

        combatTagData.setAttackedAt(System.currentTimeMillis());
        combatTagData.setAttackedBy(attacker.getUuid());

        Bukkit.getPluginManager().callEvent(new ServerDamageEvent(
                victim,
                Optional.of(attacker),
                victim.getServerable(),
                event
        ));
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;

        ProfileHandler profileHandler = plugin.getProfileHandler();

        Profile victim = profileHandler.getProfile((Player) event.getEntity());
        CombatTagData combatTagData = victim.getCombatTagData();
        Serverable serverable = victim.getServerable();
        Optional<Profile> attackerOptional;

        if (!combatTagData.isActive()
                || combatTagData.getAttackedBy() == null
                || profileHandler.getProfile(combatTagData.getAttackedBy()) == null
                || profileHandler.getProfile(combatTagData.getAttackedBy()).getServerable() != serverable) {
            attackerOptional = Optional.empty();
        } else {
            attackerOptional = Optional.of(profileHandler.getProfile(combatTagData.getAttackedBy()));
        }
        if (!(event instanceof EntityDamageByEntityEvent)) {
            Bukkit.getPluginManager().callEvent(new ServerDamageEvent(
                    victim,
                    attackerOptional,
                    victim.getServerable(),
                    event
            ));
            return;
        }

        //Bukkit.broadcastMessage("Damage by Anthing (event caller)");
    }

    @EventHandler
    private void onDamageOtherEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntity().getType() == EntityType.PLAYER) return;

        ProfileHandler profileHandler = plugin.getProfileHandler();

        Profile attacker = profileHandler.getProfile((Player) event.getDamager());

        Bukkit.getPluginManager().callEvent(new ServerDamageOtherEntityEvent(
                event.getEntity(),
                attacker,
                attacker.getServerable(),
                event
        ));
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        ProfileHandler profileHandler = plugin.getProfileHandler();

        Profile victim = profileHandler.getProfile(event.getEntity());
        CombatTagData combatTagData = victim.getCombatTagData();
        Serverable serverable = victim.getServerable();
        Optional<Profile> attackerOptional;

        if (!combatTagData.isActive()
                || combatTagData.getAttackedBy() == null
                || profileHandler.getProfile(combatTagData.getAttackedBy()) == null
                || profileHandler.getProfile(combatTagData.getAttackedBy()).getServerable() != serverable) {
            attackerOptional = Optional.empty();
        } else {
            attackerOptional = Optional.of(profileHandler.getProfile(combatTagData.getAttackedBy()));
        }
        Bukkit.getPluginManager().callEvent(new ServerDeathEvent(
                victim,
                attackerOptional,
                victim.getServerable(),
                event
        ));
    }

}
