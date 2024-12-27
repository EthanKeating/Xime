package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SGSubMenu extends FastInv {

    public SGSubMenu() {
        super(54, "&bSurvival Games");

        List<SGServerable> gameServers = plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> serverable instanceof SGServerable)
                .map(serverable -> (SGServerable) serverable)
                .sorted(Comparator
                        .comparingInt((SGServerable serverable) -> serverable.getPlayerList().size()) // Sort by player count in ascending order
                        .reversed()
                        .thenComparing(SGServerable::getGameState)) // Then sort by game state in ascending order
                .collect(Collectors.toList());

        int index = 9;
        for (SGServerable server : gameServers) {

            ItemStack serverItem = null;
            GameState gameState = server.getGameState();
            switch (gameState) {
                case LOBBY:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(5)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&aLOBBY")
                            .build();
                    break;
                case PREGAME:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(4)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&ePREGAME")
                            .build();
                    break;
                case LIVEGAME:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(4)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&eLIVEGAME")
                            .build();
                    break;
                case PREDEATHMATCH:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cPREDEATHMATCH")
                            .build();
                    break;
                case DEATHMATCH:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cDEATHMATCH")
                            .build();
                    break;
                case CLEANUP:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cCLEANUP")
                            .build();
                    break;
                case RESTARTING:
                    serverItem = new ItemBuilder(Material.STAINED_CLAY)
                            .data(15)
                            .amount(server.getPlayerList().size())
                            .name("&a" + server)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&8RESTARTING")
                            .build();
                    break;

            }

            setItem(index++, serverItem, e -> {
                ((Player) e.getWhoClicked()).performCommand("join sg " + server.getServerId());
                e.setCancelled(true);
            });
        }
    }
}
