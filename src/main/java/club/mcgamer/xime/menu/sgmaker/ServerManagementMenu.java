package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class ServerManagementMenu extends FastInv {

    public ServerManagementMenu(Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Mutators"));


        setItem(0, new ItemBuilder(Material.BOOK)
                        .name("&bConfig Templates")
                        .build(),
                e -> {

                });

        setItem(2, new ItemBuilder(Material.EMPTY_MAP)
                .name("&bMap Selection")
                .build(),
                e -> {
                    new MapSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(3, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .name("&bTeam Selection")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(4, new ItemBuilder(Material.GOLDEN_APPLE)
                        .name("&bMutators")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });
        setItem(5, new ItemBuilder(Material.SKULL_ITEM)
                        .name("&bPlayers")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(7, new ItemBuilder(Material.FIREWORK)
                        .name("&bStart Game")
                        .build(),
                e -> {
                    profile.getPlayer().performCommand("forcestart");
                });

    }
}

