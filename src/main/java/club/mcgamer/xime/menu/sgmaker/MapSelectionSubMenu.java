package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.map.impl.MapPool;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapSelectionSubMenu extends FastInv {

    public MapSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable, int page) {
        super(54, TextUtil.translate(String.format("Maps (%s) (Page %s)", profile.getPlugin().getMapHandler().getMapPool().size(), page)));

        setItem(0, new ItemBuilder(Material.WOOL)
                .data(14)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        if (page > 1) {
            setItem(1, new ItemBuilder(Material.WOOL)
                            .data(1)
                            .name("&aPrevious")
                            .build(),
                    e -> {
                        new MapSelectionSubMenu(previousMenu, profile, serverable, page - 1).open(profile.getPlayer());
                    });
        }
        setItem(8, new ItemBuilder(Material.WOOL)
                        .data(5).
                        name("&aNext")
                        .build(),
                e -> {
            new MapSelectionSubMenu(previousMenu, profile, serverable, page + 1).open(profile.getPlayer());
        });

        List<Map.Entry<String, MapData>> entrySets = new CopyOnWriteArrayList<>(profile.getPlugin().getMapHandler().getMapPool().entrySet());
        for(int i = 9; i < 54; i++) {

            int mapIndex = (i + ((54 - 9) * (page - 1))) - 9;

            if (mapIndex >= entrySets.size()) {
                break;
            }

            Map.Entry<String, MapData> mapEntry = entrySets.get(mapIndex);

            String identifier = mapEntry.getKey();
            MapData mapData = mapEntry.getValue();

            setItem(i, new ItemBuilder(Material.EMPTY_MAP)
                            .name("&a" + mapData.getMapName())
                            .lore("&6" + mapData.getMapAuthor(), "&7(" + identifier + ")")
                            .build(),
                    e -> {
                        serverable.getMapPool().setMap(identifier);
                        previousMenu.open(profile.getPlayer());
                        serverable.announce("&6Map: &e" + mapData.getMapName());
                    });

        }
    }
}

