package club.mcgamer.xime.build.menu;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class OptimizeMenu extends FastInv {
    public OptimizeMenu(Profile profile, BuildServerable serverable) {
        super(9, TextUtil.translate("Optimize"));

        setItem(2, new ItemBuilder(Material.STAINED_CLAY)
                        .data(3)
                        .amount(1)
                        .name("&aSuper Small Map ")
                        .lore("&7Use this first if server is crashing")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();
                    serverable.optimize(10);
                });
        setItem(4, new ItemBuilder(Material.STAINED_CLAY)
                        .data(5)
                        .amount(1)
                        .name("&aSmall Map ")
                        .lore("&7Use this option on most maps")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();
                    serverable.optimize(20);
                });
        setItem(6, new ItemBuilder(Material.STAINED_CLAY)
                        .data(6)
                        .amount(1)
                        .name("&cLARGE Map ")
                        .lore("&7This will cause an extra bit of lag, only use this when Small cant be used.")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();
                    serverable.optimize(32);
                });
    }
}
