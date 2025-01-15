package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class TeamSelectionSubMenu extends FastInv {

    public TeamSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Team Selection"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        for(int index = 0; index < TeamType.values().length; index++) {
            TeamType type = TeamType.values()[index];

            setItem(index + 2, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + type.getName())
                            .build(),
                    e -> {
                        serverable.announce("&6Team Mode: &e" + type.getName());
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

