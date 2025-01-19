package club.mcgamer.xime.build.menu;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class MapSaveMenu extends FastInv {

    public MapSaveMenu(Profile profile, BuildServerable serverable) {
        super(9, TextUtil.translate("Map Data Menu"));

        setItem(2, new ItemBuilder(Material.STAINED_CLAY)
                .data(5)
                        .amount(1)
                        .name("&aSave Map Changes")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();

                    MapData.save(serverable.getWorldName(), serverable.getMapData());
                    serverable.save();
                    serverable.stop();
                    profile.sendMessage("&8[&3Xime&8] &aSaved changes for &f" + serverable.getMapData().getMapName());
                });
        setItem(6, new ItemBuilder(Material.STAINED_CLAY)
                        .data(6)
                        .amount(1)
                        .name("&cDiscard Map Changes")
                        .lore("&7This will only discard BUILD changes, not MapData changes")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();

                    profile.sendMessage("&8[&3Xime&8] &aDiscarded changes for &f" + serverable.getMapData().getMapName());
                    serverable.discard();
                    serverable.stop();
                });
    }
}

