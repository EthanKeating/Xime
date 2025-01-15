package club.mcgamer.xime.menu.sgmaker.mutators;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class LootStyleSelectionSubMenu extends FastInv {

    public LootStyleSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Loot Style"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        for(int index = 0; index < LootStyle.values().length; index++) {
            LootStyle style = LootStyle.values()[index];

            setItem(index + 2, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + style.getName())
                            .build(),
                    e -> {
                        serverable.announce("&6Loot Style: &e" + style.getName());
                        serverable.getGameSettings().setLootStyle(style);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

