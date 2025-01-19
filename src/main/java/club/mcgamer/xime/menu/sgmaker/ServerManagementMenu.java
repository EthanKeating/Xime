package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.runnable.MakerLobbyRunnable;
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
                    new MapSelectionSubMenu(this, profile, serverable, 1).open(profile.getPlayer());
                });

        setItem(3, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .name("&bTeam Selection")
                        .build(),
                e -> {
                    new TeamSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(4, new ItemBuilder(Material.GOLDEN_APPLE)
                        .name("&bMutators")
                        .build(),
                e -> {
                    new MutatorsSubMenu(this, profile, serverable).open(profile.getPlayer());
                });
        setItem(5, new ItemBuilder(Material.SKULL_ITEM)
                        .name("&bPrivacy")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(7, new ItemBuilder(Material.FIREWORK)
                        .name("&bStart Game")
                        .build(),
                e -> {
                    if (serverable.getCurrentRunnable() instanceof MakerLobbyRunnable) {
                        MakerLobbyRunnable lobbyRunnable = (MakerLobbyRunnable) serverable.getCurrentRunnable();

                        lobbyRunnable.setStartGame(true);
                        serverable.announce("&3The host has started the countdown timer&8.");
                        profile.getPlayer().closeInventory();
                    }
                });

    }
}

