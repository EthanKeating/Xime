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

public class NoHitDelaySelectionSubMenu extends FastInv {

    public NoHitDelaySelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("No Hit Delay"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, Boolean>> toggles = Arrays.asList(
                new Pair<>("Enabled", true),
                new Pair<>("Disabled", false)
        );

        int index = 3;
        for(Pair<String, Boolean> togglePairs : toggles) {
            String toggleDisplay = togglePairs.getKey();
            boolean toggle = togglePairs.getValue();

            setItem(index, new ItemBuilder(Material.STAINED_CLAY)
                            .data(toggle ? 5 : 14)
                            .amount(1)
                            .name("&b" + toggleDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6No Hit Delay: &e" + toggleDisplay);
                        serverable.getGameSettings().setNoHitDelay(toggle);
                        previousMenu.open(profile.getPlayer());
                    });
            index += 2;
        }
    }
}

