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

public class DeathmatchThresholdSelectionSubMenu extends FastInv {

    public DeathmatchThresholdSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Deathmatch Threshold"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Integer>> thresholds = Arrays.asList(
                new Pair<>("None", 0),
                new Pair<>("2 players", 2),
                new Pair<>("3 players", 3),
                new Pair<>("4 players", 4),
                new Pair<>("6 players", 6),
                new Pair<>("12 players", 12)
        );

        int index = 2;
        for(Pair<String, Integer> thresholdPairs : thresholds) {
            String thresholdDisplay = thresholdPairs.getKey();
            int threshold = thresholdPairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + thresholdDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Deathmatch Threshold: &e" + thresholdDisplay);
                        serverable.getGameSettings().setDeathmatchPlayers(threshold);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

