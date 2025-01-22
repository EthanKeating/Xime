package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.hub.data.HubSpeed;
import club.mcgamer.xime.hub.data.HubTemporaryData;
import club.mcgamer.xime.menu.MenuHandler;
import club.mcgamer.xime.menu.hub.HubMainMenu;
import club.mcgamer.xime.menu.hub.HubSelectorMenu;
import club.mcgamer.xime.menu.hub.SGSubMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.server.event.ServerItemInteractEvent;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Vector;

public class HubItemInteractListener extends IListener {

    @EventHandler
    private void onHubItemMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof HubServerable) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPressurePlateStep(ServerInteractEvent event) {

        Profile profile = event.getProfile();

        if (profile.getServerable() instanceof HubServerable) {
            if (event.getEvent().getAction() == Action.PHYSICAL) {
                // Get the block the player stepped on
                if (event.getEvent().getClickedBlock() != null && event.getEvent().getClickedBlock().getType() == Material.STONE_PLATE)
                    launchPlayerForward(profile.getPlayer());

            }
        }
    }

    @EventHandler
    private void onHubItemInteract(ServerItemInteractEvent event) {
        if (event.getServerable() instanceof HubServerable) {

            HubServerable hubServerable  = (HubServerable) event.getServerable();
            Profile profile = event.getProfile();
            Player player = profile.getPlayer();
            Material type = event.getItemStack().getType();
            MenuHandler menuHandler = plugin.getMenuHandler();

            HubTemporaryData hubTemporaryData = (HubTemporaryData) profile.getTemporaryData();

            switch(type) {
                case COMPASS:
                    new HubMainMenu(profile).open(player);
                    break;
                case WATCH:

                    hubTemporaryData.setHidePlayers(!hubTemporaryData.isHidePlayers());

                    if (hubTemporaryData.isHidePlayers()) {
                        profile.sendMessage("&8[&eMCGamer&8] &aAll players are now hidden.");
                        hubServerable.getPlayerList().forEach(loopPlayer -> player.hidePlayer(loopPlayer.getPlayer()));
                    } else {
                        profile.sendMessage("&8[&eMCGamer&8] &aAll players are now shown.");
                        hubServerable.getPlayerList().forEach(loopPlayer -> player.showPlayer(loopPlayer.getPlayer()));
                    }
                    break;
                case MINECART:
                    HubSpeed newHubSpeed;

                    switch (hubTemporaryData.getHubSpeed()) {
                        case SPEEDY:
                            newHubSpeed = HubSpeed.SUPER_SPEEDY;
                            player.setWalkSpeed(0.6f);
                            break;
                        case SUPER_SPEEDY:
                            newHubSpeed = HubSpeed.NORMAL;
                            player.setWalkSpeed(0.2f);
                            break;
                        default:
                            newHubSpeed = HubSpeed.SPEEDY;
                            player.setWalkSpeed(0.4f);
                            break;
                    }
                    hubTemporaryData.setHubSpeed(newHubSpeed);
                    profile.sendMessage(String.format("&8[&eMCGamer&8] &fMovement speed set to %s.", newHubSpeed.getName()));

                    break;
                case EMERALD: //https://mcgamer.club/store/
                    TextComponent message = new TextComponent(TextUtil.translate("&8[&eMCGamer&8] &fClick "));
                    TextComponent linkSection = new TextComponent(TextUtil.translate("&6&nhere&f"));
//                    serverSection.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
//                            new TextComponent(ColorUtil.translate(String.format("&e%s &f(%s)", serverable, serverable.getPlayers().size() + " player" + (serverable.getPlayers().size() == 1 ? "" : "s")))),
//                            new TextComponent(ColorUtil.translate("\n\n&6Click to connect to this server"))
//                    }));
                    linkSection.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://mcgamer.club/store/"));
                    message.addExtra(linkSection);
                    message.addExtra(TextUtil.translate("&f to open the web store!"));

                    player.spigot().sendMessage(message);
                    break;
                case NETHER_STAR:
                    new HubSelectorMenu(profile).open(player);
                    break;
            }

        }
    }

    private void launchPlayerForward(Player player) {
        // Get the player's current velocity
        Vector velocity = player.getVelocity();

        velocity.setY(2.0);

        float yaw = player.getLocation().getYaw();
        double radYaw = Math.toRadians(yaw);
        double forwardSpeed = 3.1;

        velocity.setX(-Math.sin(radYaw) * forwardSpeed);
        velocity.setZ(Math.cos(radYaw) * forwardSpeed);

        // Set the new velocity for the player
        player.setVelocity(velocity);
    }

}
