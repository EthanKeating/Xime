package club.mcgamer.xime.util;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;

@UtilityClass
public class PlayerUtil {
    public void refresh(Profile profile) {
        Player player = profile.getPlayer();
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

        for(PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setNoDamageTicks(20);
        player.setMaximumNoDamageTicks(20);
        player.setSaturation(10f);
        player.setFoodLevel(20);
        player.setMaxHealth(20f);
        player.setHealth(20f);
        player.setWalkSpeed(0.2f);
        player.setFireTicks(0);
        player.setNoDamageTicks(0);
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
        player.getInventory().setHeldItemSlot(0);

        player.getEquipment().clear();
        player.getInventory().clear();

        ItemStack airItem = new ItemStack(Material.AIR);
        player.getInventory().setHelmet(airItem);
        player.getInventory().setChestplate(airItem);
        player.getInventory().setLeggings(airItem);
        player.getInventory().setBoots(airItem);
        for(int i = 0; i < 27; i++)
            player.getInventory().setItem(i, airItem);
    }

    public void unsetGamemode(Profile profile) {
        profile.getUser().sendPacket(new WrapperPlayServerChangeGameState(3, -1));
    }

    public String getHealth(Profile profile) {
        return ChatColor.RED + new DecimalFormat("#000.0").format(Math.max(0.5, Math.round(profile.getPlayer().getHealth()) / 2F));
    }

    public void setFlying(Profile profile) {
        Player player = profile.getPlayer();

        profile.getUser().sendPacket(new WrapperPlayServerPlayerAbilities(
                false, true, true, false, 0.05f, 0.1f
        ));
    }
}
