package club.mcgamer.xime.listener.sgmaker;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.hub.data.HubSpeed;
import club.mcgamer.xime.hub.data.HubTemporaryData;
import club.mcgamer.xime.menu.MenuHandler;
import club.mcgamer.xime.menu.hub.HubSelectorMenu;
import club.mcgamer.xime.menu.hub.SGSubMenu;
import club.mcgamer.xime.menu.sgmaker.ServerManagementMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

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
            }

        }
    }

}
