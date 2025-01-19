package club.mcgamer.xime.listener.build;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.util.IListener;
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
            player.setGameMode(GameMode.CREATIVE);
            player.setAllowFlight(true);
            PlayerUtil.setFlying(profile);
            PlayerUtil.unsetGamemode(profile);

            profile.sendMessage("&8[&3Xime&8] &bWelcome to the map editor!");
            profile.sendMessage("");
            profile.sendMessage(" &eFollow the numbering of the items");
            profile.sendMessage(" &eTo easily setup an SG Map!");
            profile.sendMessage("");
            profile.sendMessage("&aYou can get these items back at any time by doing /items");

            serverable.mainItems(player);
        }
    }

}
