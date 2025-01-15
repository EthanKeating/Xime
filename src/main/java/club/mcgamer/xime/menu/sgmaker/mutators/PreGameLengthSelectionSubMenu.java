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

public class PreGameLengthSelectionSubMenu extends FastInv {

    public PreGameLengthSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Game Length"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Integer>> lootTables = Arrays.asList(
                new Pair<>("5 seconds", 5),
                new Pair<>("10 seconds", 10),
                new Pair<>("15 seconds", 15),
                new Pair<>("20 seconds", 20),
                new Pair<>("30 seconds", 30)
        );

        int index = 2;
        for(Pair<String, Integer> gameLengthPairs : lootTables) {
            String gameLengthDisplay = gameLengthPairs.getKey();
            int gameLength = gameLengthPairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + gameLengthDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Pre Game Length: &e" + gameLengthDisplay);
                        serverable.getGameSettings().setPreGameLength(gameLength);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

