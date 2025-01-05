package club.mcgamer.xime.listener.build;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BuildJoinListener extends IListener {

    @EventHandler
    private void onBuildJoin(ServerJoinEvent event) {
        if (event.getServerable() instanceof BuildServerable) {
            BuildServerable serverable = (BuildServerable) event.getServerable();

            World world = serverable.getWorld();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();

            PlayerUtil.refresh(profile);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
            player.setFlying(true);
            PlayerUtil.unsetGamemode(profile);

            player.getInventory().setItem(0, new ItemBuilder(Material.SKULL_ITEM)
                    .name("&b&lSet Spectator Spawn &7&o(Map Center)")
                    .build());

            player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM)
                    .name("&b&lAdd a Spawn")
                    .data(3)
                    .build());

            player.getInventory().setItem(2, new ItemBuilder(Material.ANVIL)
                    .name("&b&lSet Map Name")
                    .build());

            player.getInventory().setItem(3, new ItemBuilder(Material.NAME_TAG)
                    .name("&b&lSet Map Author(s)")
                    .build());

            player.getInventory().setItem(4, new ItemBuilder(Material.EYE_OF_ENDER)
                    .name("&b&lSet Map Link")
                    .build());

            player.getInventory().setItem(5, new ItemBuilder(Material.CHEST)
                    .name("&b&lScan for Chests")
                    .build());

            player.getInventory().setItem(6, new ItemBuilder(Material.GLASS)
                    .name("&b&lRemove useless blocks")
                    .build());

            player.getInventory().setItem(7, new ItemBuilder(Material.STAINED_CLAY)
                    .name("&c&lDiscard Map Changes")
                    .data(6)
                    .build());
            player.getInventory().setItem(8, new ItemBuilder(Material.STAINED_CLAY)
                    .name("&a&lSave Map Changes")
                    .data(5)
                    .build());
        }
    }

}
