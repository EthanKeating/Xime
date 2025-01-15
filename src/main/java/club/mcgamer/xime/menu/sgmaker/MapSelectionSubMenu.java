package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

import java.util.Map;

public class MapSelectionSubMenu extends FastInv {

    public MapSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(54, TextUtil.translate("Maps (Page 1)"));

        setItem(0, new ItemBuilder(Material.WOOL)
                .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        int index = 9;
        for(Map.Entry<String, MapData> mapEntry : MapPool.getAllMaps().entrySet()) {
            String identifier = mapEntry.getKey();
            MapData mapData = mapEntry.getValue();

            setItem(index++, new ItemBuilder(Material.EMPTY_MAP)
                            .name("&a" + mapData.getMapName())
                            .lore("&6" + mapData.getMapAuthor())
                            .build(),
                    e -> {
                        serverable.getMapPool().setMap(identifier);
                        previousMenu.open(profile.getPlayer());
                        serverable.announce("&6Map: &e" + mapData.getMapName());
                    });

        }
    }
}

