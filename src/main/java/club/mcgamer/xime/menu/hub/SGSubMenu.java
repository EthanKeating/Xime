package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.staff.StaffServerable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SGSubMenu extends FastInv {

    public SGSubMenu(Profile profile) {
        super(54, "&bSurvival Games");

        List<SGServerable> gameServers = plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> serverable instanceof SGServerable)
                .filter(serverable -> !(serverable instanceof SGMakerServerable))
                .map(serverable -> (SGServerable) serverable)
                .sorted(Comparator
                        .comparingInt((SGServerable serverable) -> serverable.getGameState().ordinal())
                        .reversed()// Sort by player count in ascending order
                        .thenComparing(serverable -> serverable.getPlayerList().size())
                        .reversed()) // Then sort by game state in ascending order
                .collect(Collectors.toList());

        setItem(3, new ItemBuilder(Material.BOW).name("&aSurvival Games").build(), e -> {
            e.setCancelled(true);
        });

        setItem(4, new ItemBuilder(Material.SKULL_ITEM).data(3).owner(profile.getPlayer()).name("&bGame Menu").build(), e -> {
            e.setCancelled(true);
            new HubMainMenu(profile).open(profile.getPlayer());
        });

        setItem(5, new ItemBuilder(Material.BOW).name("&aSurvival Games").build(), e -> {
            e.setCancelled(true);
        });

        int index = 9;
        for (SGServerable server : gameServers) {

            ItemStack serverItem = null;
            GameState gameState = server.getGameState();
            String serverName = server.toString().replace('-', ' ');

            if (profile.getServerable() instanceof StaffServerable) {
                serverItem = new ItemBuilder(Material.STAINED_CLAY)
                        .data(4)
                        .amount(Math.max(1, server.getPlayerList().size()))
                        .name("&a" + serverName)
                        .lore(String.format("&fPlayers: &e%s/%s", server.getPlayerList().size(), server.getMaxPlayers()),
                                "&fState: &e", gameState.getName())
                        .build();
            } else {
                serverItem = switch (gameState) {
                    case LOBBY -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(5)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&aLOBBY")
                            .build();
                    case PREGAME -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(4)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&ePREGAME")
                            .build();
                    case LIVEGAME -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(4)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&eLIVEGAME")
                            .build();
                    case PREDEATHMATCH -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cPREDEATHMATCH")
                            .build();
                    case DEATHMATCH -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cDEATHMATCH")
                            .build();
                    case ENDGAME -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cENDGAME")
                            .build();
                    case CLEANUP -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(14)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&cCLEANUP")
                            .build();
                    case RESTARTING -> new ItemBuilder(Material.STAINED_CLAY)
                            .data(15)
                            .amount(Math.max(1, server.getPlayerList().size()))
                            .name("&a" + serverName)
                            .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&8RESTARTING")
                            .build();
                    default -> null;
                };

            }

            setItem(index++, serverItem, e -> {
                if (profile.getServerable() instanceof StaffServerable) {
                    profile.getPlayer().teleport(server.getWorld().getSpawnLocation());
                    return;
                }
                profile.getPlayer().performCommand("join sg " + server.getServerId());
                e.setCancelled(true);
            });
        }
    }
}
