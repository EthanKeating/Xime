package club.mcgamer.xime.menu.sgmaker.mutators;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class GracePeriodSelectionSubMenu extends FastInv {

    public GracePeriodSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Grace Period"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Integer>> gracePeriods = Arrays.asList(
                new Pair<>("None", -1),
                new Pair<>("30 seconds", 30),
                new Pair<>("1 minute", 60),
                new Pair<>("2 minutes", 2 * 60),
                new Pair<>("3 minutes", 3 * 60),
                new Pair<>("4 minutes", 4 * 60),
                new Pair<>("5 minutes", 5 * 60)
        );

        int index = 2;
        for(Pair<String, Integer> gracePeriodPairs : gracePeriods) {
            String gracePeriodDisplay = gracePeriodPairs.getKey();
            int gracePeriod = gracePeriodPairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + gracePeriodDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Grace Period: &e" + gracePeriodDisplay);
                        serverable.getGameSettings().setGracePeriodTime(gracePeriod);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

