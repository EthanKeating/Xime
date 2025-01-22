package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BGSubMenu extends FastInv {

    public BGSubMenu(Profile profile) {
        super(54, "&bSurvival Games");

        List<BGServerable> gameServers = plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> serverable instanceof BGServerable)
                .map(serverable -> (BGServerable) serverable)
                .sorted(Comparator.comparingInt((BGServerable serverable) -> serverable.getPlayerList().size())
                                .reversed()).toList();

        setItem(3, new ItemBuilder(Material.IRON_SWORD).name("&aBattlegrounds").build(), e -> {
            e.setCancelled(true);
        });

        setItem(4, new ItemBuilder(Material.SKULL_ITEM).data(3).owner(profile.getPlayer()).name("&bGame Menu").build(), e -> {
            e.setCancelled(true);
            new HubMainMenu(profile).open(profile.getPlayer());
        });

        setItem(5, new ItemBuilder(Material.IRON_SWORD).name("&aBattlegrounds").build(), e -> {
            e.setCancelled(true);
        });

        int index = 9;
        for (BGServerable server : gameServers) {

            String serverName = server.toString().replace('-', ' ');
            ItemStack serverItem = new ItemBuilder(Material.STAINED_CLAY)
                    .data(4)
                    .amount(Math.max(1, server.getPlayerList().size()))
                    .name("&a" + serverName)
                    .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&aINGAME")
                    .build();

            if (server.getWorld() == null) {
                serverItem = new ItemBuilder(Material.STAINED_CLAY)
                        .data(15)
                        .amount(Math.max(1, server.getPlayerList().size()))
                        .name("&a" + serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", server.getPlayerList().size(), server.getMaxPlayers()), "", "&aLOADING")
                        .build();
            }

            setItem(index++, serverItem, e -> {
                ((Player) e.getWhoClicked()).performCommand("join bg " + server.getServerId());
                e.setCancelled(true);
            });
        }
    }
}
