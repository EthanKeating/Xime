package club.mcgamer.xime.menu.sg;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpectateMenu extends FastInv {

    public static final ItemStack SPECTATE_ITEM = new ItemBuilder(Material.PAINTING).name("&bSpectate Menu").build();

    public SpectateMenu(Profile profile, SGServerable serverable, int page) {
        super(18 + (9 * (serverable.getTributeList().size() / 9)), TextUtil.translate(String.format("Choose a player", profile.getPlugin().getMapHandler().getMapPool().size(), page)));

        for (int i = 0; i < 9; i++) {
            setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .data(15)
                            .name("")
                            .build(),
                    e -> {
                        e.setCancelled(true);
                    });
        }
        List<Profile> players = serverable.getTributeList();
        for (int i = 0; i < players.size(); i++) {
            int index = 9 + i;
            Profile loopProfile = players.get(i);

            setItem(index, new ItemBuilder(Material.SKULL_ITEM)
                            .data(3)
                            .owner(loopProfile.getPlayer())
                            .name(loopProfile.getDisplayName())
                            .lore("&eClick to teleport to " + loopProfile.getDisplayName()).build(),
                    e -> {
                        profile.getPlayer().performCommand("spectate " + loopProfile.getName());
                    });
        }
    }
}

