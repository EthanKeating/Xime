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

public class MaxHealSelectionSubMenu extends FastInv {

    public MaxHealSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Max Health"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Double>> maxHealths = Arrays.asList(
                new Pair<>("1 Heart", 1 * 2.0),
                new Pair<>("5 Hearts", 5 * 2.0),
                new Pair<>("10 Hearts (Default)", 10 * 2.0),
                new Pair<>("15 Hearts", 15 * 2.0),
                new Pair<>("20 Hearts", 20 * 2.0),
                new Pair<>("40 hearts", 40 * 2.0)
        );

        int index = 2;
        for(Pair<String, Double> maxHealthPairs : maxHealths) {
            String maxHealthDisplay = maxHealthPairs.getKey();
            double maxHealth = maxHealthPairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + maxHealthDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Max Health: &e" + maxHealthDisplay);
                        serverable.getGameSettings().setMaxHealth(maxHealth);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

