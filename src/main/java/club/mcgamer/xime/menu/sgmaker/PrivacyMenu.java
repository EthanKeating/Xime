package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.sgmaker.runnable.MakerLobbyRunnable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class PrivacyMenu extends FastInv {

    public PrivacyMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Privacy"));


        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(14)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        setItem(3, new ItemBuilder(Material.COMPASS)
                        .name("&bToggle Privacy")
                        .lore(serverable.getPrivacyMode() == PrivacyMode.PRIVATE ? "&cCurrently Private" : "&aCurrently Public").build(),
                e -> {

                    serverable.setPrivacyMode(serverable.getPrivacyMode().getNext());
                    serverable.announce("&6Privacy: &e" + (serverable.getPrivacyMode() == PrivacyMode.PRIVATE ? "&cPrivate" : "&aPublic"));
                    new PrivacyMenu(previousMenu, profile, serverable).open(profile.getPlayer());
                });
        setItem(5, new ItemBuilder(Material.SKULL_ITEM)
                .data(3)
                        .name("&bManage Players").build(),
                e -> {
                    new PlayerMenu(previousMenu, profile, serverable, 0).open(profile.getPlayer());
                });


    }
}

