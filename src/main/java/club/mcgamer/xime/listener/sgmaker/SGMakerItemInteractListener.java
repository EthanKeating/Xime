package club.mcgamer.xime.listener.sgmaker;

import club.mcgamer.xime.menu.sgmaker.ServerManagementMenu;
import club.mcgamer.xime.menu.sgmaker.TeamSelectionSubMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class SGMakerItemInteractListener extends IListener {
    @EventHandler
    private void onHubItemInteract(ServerItemInteractEvent event) {
        if (event.getServerable() instanceof SGMakerServerable) {

            SGMakerServerable serverable  = (SGMakerServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            Material type = event.getItemStack().getType();

            switch(type) {
                case NETHER_STAR:
                    new ServerManagementMenu(profile, serverable).open(player);
                    break;
                case LEATHER_CHESTPLATE:
                    event.getEvent().setCancelled(true);
                    if (serverable.getGameState() == GameState.LOBBY) {
                        new TeamSelectionSubMenu(profile, serverable).open(player);
                        profile.getPlayer().updateInventory();
                        profile.getPlayer().getInventory().setChestplate(new ItemStack(Material.STONE));
                        profile.getPlayer().updateInventory();
                        profile.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
                        profile.getPlayer().updateInventory();
                        break;
                    }
                    break;
            }

        }
    }

}
