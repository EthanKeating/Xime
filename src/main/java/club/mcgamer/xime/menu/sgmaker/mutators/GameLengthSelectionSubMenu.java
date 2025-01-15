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

public class GameLengthSelectionSubMenu extends FastInv {

    public GameLengthSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Game Length"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Integer>> lootTables = Arrays.asList(
                new Pair<>("5 minutes", 5 * 60),
                new Pair<>("10 minutes", 10 * 60),
                new Pair<>("15 minutes", 15 * 60),
                new Pair<>("20 minutes", 20 * 60),
                new Pair<>("30 minutes", 30 * 60),
                new Pair<>("60 minutes", 60 * 60)
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
                        serverable.announce("&6Game Length: &e" + gameLengthDisplay);
                        serverable.getGameSettings().setLiveGameLength(gameLength);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

