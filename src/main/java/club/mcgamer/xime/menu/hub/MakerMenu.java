package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MakerMenu extends FastInv {

    public MakerMenu(Profile profile) {
        super(9, "Custom Server Setup");

        ItemStack serverItem = new ItemBuilder(Material.DIAMOND)
                .amount(1)
                .name("&bMCSG Maker Game (Premium)")
                .lore("&6Start a game with your daily free total.",
                        "&eDaily remaining today: &a" + (profile.getPlayer().hasPermission("xime.iron") ? "Unlimited" : "0"),
                        "&eGames remaining this hour: &a" + (profile.getPlayer().hasPermission("xime.iron") ? "Unlimited" : "0"))
                .build();

        setItem(4, serverItem, e -> {
            ((Player) e.getWhoClicked()).performCommand("maker");
            e.setCancelled(true);
        });

    }


}
