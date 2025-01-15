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

public class RodSpeedSelectionSubMenu extends FastInv {

    public RodSpeedSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Rod Speed"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Double>> rodSpeeds = Arrays.asList(
                new Pair<>("1.0x", 1.0),
                new Pair<>("1.1x", 1.1),
                new Pair<>("1.25x", 1.25),
                new Pair<>("1.5x", 1.5),
                new Pair<>("1.75x", 1.75),
                new Pair<>("2.0x", 2.0),
                new Pair<>("3.0x", 3.0)
        );

        int index = 2;
        for(Pair<String, Double> rodSpeedPairs : rodSpeeds) {
            String rodSpeedDisplay = rodSpeedPairs.getKey();
            double rodSpeed = rodSpeedPairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + rodSpeedDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Rod Speed: &e" + rodSpeedDisplay);
                        serverable.getGameSettings().setRodSpeedMultiplier(rodSpeed);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

