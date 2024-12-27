package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.menu.MenuHandler;
import club.mcgamer.xime.menu.hub.HubSelectorMenu;
import club.mcgamer.xime.menu.hub.SGSubMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HubItemInteractListener extends IListener {

    @EventHandler
    private void onHubItemInteract(ServerItemInteractEvent event) {
        if (event.getServerable() instanceof HubServerable) {

            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            Material type = event.getItemStack().getType();
            MenuHandler menuHandler = plugin.getMenuHandler();

            switch(type) {
                case COMPASS:
                    new SGSubMenu().open(player);
                    break;
                case WATCH:
                case MINECART:
                case NETHER_STAR:
                    new HubSelectorMenu(profile).open(player);
                    break;
            }

        }
    }

}
