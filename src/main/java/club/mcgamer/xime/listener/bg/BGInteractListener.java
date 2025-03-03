package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerHungerLossEvent;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BGInteractListener extends IListener {

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!(profile.getServerable() instanceof BGServerable serverable)) return;
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();

        if (!(block.getState() instanceof Sign sign)) return;

        serverable.setFighting(profile, sign.getLine(2).toLowerCase());
    }

    @EventHandler
    private void onBGInteract(ServerItemInteractEvent event) {
        if (event.getServerable() instanceof BGServerable serverable) {
            Profile profile = event.getProfile();
            BGTemporaryData temporaryData = (BGTemporaryData) profile.getTemporaryData();

            if (event.getItemStack().getType() == Material.BOOK
                    && event.getItemStack().getItemMeta().getDisplayName().contains("Loadout Editor")
                    && temporaryData.isWaiting() ) {

                serverable.getLoadoutMenu().open(profile);
            }
        }
    }

    @EventHandler
    private void onHungerLoss(ServerHungerLossEvent event) {
        if (event.getServerable() instanceof BGServerable serverable) {
            event.getEvent().setCancelled(true);
            event.getEvent().setFoodLevel(20);
        }
    }

}
