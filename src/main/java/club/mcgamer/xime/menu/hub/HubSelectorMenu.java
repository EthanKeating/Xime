package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.ItemBuilder;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HubSelectorMenu extends FastInv {

    public HubSelectorMenu(Profile profile) {
        super(18, "MCGamer Hubs");

        List<HubServerable> hubServers = plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> serverable instanceof HubServerable)
                .map(serverable -> (HubServerable) serverable)
                .sorted(Comparator.comparing(Serverable::getServerId))
                .collect(Collectors.toList());

        int index = 0;
        for (HubServerable server : hubServers) {

            List<String> currentLore = new ArrayList<>();

            if (profile.getServerable() == server)
                currentLore.add("&fYou are already connected to this server&8.");

            currentLore.add(String.format( "&8[&e%s&8/&e%s&8] &fPlayers Online&8.", server.getPlayerList().size(), server.getMaxPlayers()));

            ItemStack serverItem = new ItemBuilder(Material.EYE_OF_ENDER)
                    .amount(server.getPlayerList().size())
                    .name("&6" + server)
                    .lore(currentLore)
                    .build();

            setItem(index++, serverItem, e -> {
                ((Player) e.getWhoClicked()).performCommand("hub " + server.getServerId());
                e.setCancelled(true);
            });
        }
    }


}
