package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HubJoinListener extends IListener {

    @EventHandler
    private void onHubJoin(ServerJoinEvent event) {
        Profile profile = event.getProfile();

        if (profile.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) profile.getServerable();

            Player player = profile.getPlayer();

            PlayerUtil.refresh(profile);
            player.setLevel(serverable.getServerId());
            player.teleport(serverable.getSpawnLocation());

            player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                    .name("&b&lQuick Teleport &7- Right click to teleport!")
                    .build());

            player.getInventory().setItem(1, new ItemBuilder(Material.WATCH)
                    .name("&5&lShow/Hide Players &7- Right click to show/hide players!")
                    .build());

            player.getInventory().setItem(2, new ItemBuilder(Material.MINECART)
                    .name("&0&lToggle Speed &7- Right click to change your walking speed!")
                    .build());

            player.getInventory().setItem(6, new ItemBuilder(Material.EMERALD)
                    .name("&2&lWebstore &7- Right click to go to webstore!")
                    .build());

            player.getInventory().setItem(7, new ItemBuilder(Material.GOLD_INGOT)
                    .name("&e&lMCGamer Credit Shop &7- Right click to access the shop!")
                    .build());

            player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR)
                    .name("&c&lLobby Selector &7- Right click to switch lobbies!")
                    .build());

            profile.sendMessage("&8[&3Xime&8] &bAccess the shop with the &6Gold Bar&8!")
                    .sendMessage("&8[&3Xime&8] &bYou have &61000 &bMCGamer Hub Credits&8.");

            if(profile.getPlayerData().isCanFly())
                player.setAllowFlight(true);

            Bukkit.getScheduler().runTaskLater(plugin, () -> profile.sendTitle("&6Hello " + profile.getName(), "&2Welcome to the &6MCGamer Club&2!", 10, 80, 10), 5);
        }
    }

}

