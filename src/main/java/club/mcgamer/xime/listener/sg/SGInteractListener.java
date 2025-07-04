package club.mcgamer.xime.listener.sg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.map.impl.MapLocation;
import club.mcgamer.xime.menu.sg.SpectateMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerAirInteractEvent;
import club.mcgamer.xime.server.event.ServerHungerLossEvent;
import club.mcgamer.xime.server.event.ServerInteractEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class SGInteractListener extends IListener {

    @EventHandler
    private void onSGItemMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            if (serverable.getSpectatorList().contains(profile)) {
                event.setCancelled(true);
            }

            switch (serverable.getGameState()) {
                case LOBBY:
                case PREGAME:
                case LOADING:
                case RESTARTING:
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onSGBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfile(player);
        if (profile.getServerable() instanceof SGServerable serverable) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onSGBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileHandler().getProfile(player);
        if (profile.getServerable() instanceof SGServerable serverable) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onSGInteractItem(ServerInteractEvent event) {
        Profile profile = event.getProfile();
        Player player = profile.getPlayer();

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            if (event.getEvent().getAction() == Action.PHYSICAL)
                return;

            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                switch (serverable.getGameState()) {
                    case PREGAME:
                    case LIVEGAME:
                    case PREDEATHMATCH:
                    case DEATHMATCH:

                        if (event.getItemStack() != null && event.getItemStack().getType() == SpectateMenu.SPECTATE_ITEM.getType()) {
                            new SpectateMenu(profile, serverable, 0).open(player);
                            return;
                        }

                        player.performCommand("spectate");
                }
            }
        }
    }

    @EventHandler
    private void onSGInteract(ServerAirInteractEvent event) {
        Profile profile = event.getProfile();

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            if (serverable.getSpectatorList().contains(profile))
                event.getEvent().setCancelled(true);
        }
    }

    @EventHandler
    private void onHungerLoss(ServerHungerLossEvent event) {
        if (event.getServerable() instanceof SGServerable serverable) {
            if(serverable.getSpectatorList().contains(event.getProfile())) {
                event.getEvent().setCancelled(true);
                event.getEvent().setFoodLevel(20);
                return;
            }
            if (serverable.getGameState() == GameState.LOBBY
                    || serverable.getGameState() == GameState.PREGAME
                    || serverable.getGameState() == GameState.PREDEATHMATCH
                    || serverable.getGameState() == GameState.ENDGAME
                    || serverable.getGameState() == GameState.CLEANUP) {
                event.getEvent().setCancelled(true);
                event.getEvent().setFoodLevel(20);
            }
        }
    }



    @EventHandler
    private void onSGInteract(ServerInteractEvent event) {

        if (event.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) event.getServerable();
            Profile profile = event.getProfile();

            switch (serverable.getGameState()) {
                case LOBBY:
                case LOADING:
                case PREGAME:
                case RESTARTING:
                    event.getEvent().setCancelled(true);
                    return;
            }
            if (serverable.getSpectatorList().contains(profile)) {
                event.getEvent().setCancelled(true);
                return;
            }

            if (event.getEvent().getClickedBlock() != null && event.getEvent().getClickedBlock().getType() == Material.CHEST) {
                Block block = event.getEvent().getClickedBlock();
                MapLocation mapLocation = new MapLocation(block.getX(), block.getY(), block.getZ());

                if (!serverable.getOpenedChestLocations().contains(mapLocation)) {
                    SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();
                    PlayerData playerData = profile.getPlayerData();

                    if (!(serverable instanceof SGMakerServerable)) {
                        temporaryData.setChestCount(temporaryData.getChestCount() + 1);
                        playerData.setSgChests(playerData.getSgChests() + 1);

                        if (playerData.getSgMostChests() < temporaryData.getChestCount())
                            playerData.setSgMostChests(temporaryData.getChestCount());
                    }

                    serverable.getOpenedChestLocations().add(mapLocation);
                }
            }

        }
    }
}
