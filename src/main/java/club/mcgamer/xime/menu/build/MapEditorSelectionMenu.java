package club.mcgamer.xime.menu.build;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapEditorSelectionMenu extends FastInv {

    public MapEditorSelectionMenu(Profile profile, int page) {
        super(54, TextUtil.translate(String.format("Maps (%s) (Page %s)", profile.getPlugin().getMapHandler().getMapPool().size(), page)));

        if (page > 1) {
            setItem(0, new ItemBuilder(Material.WOOL)
                            .data(1)
                            .name("&aPrevious")
                            .build(),
                    e -> {
                        new MapEditorSelectionMenu(profile, page - 1).open(profile.getPlayer());
                    });
        }
        setItem(8, new ItemBuilder(Material.WOOL)
                        .data(5).
                        name("&aNext")
                        .build(),
                e -> {
                    new MapEditorSelectionMenu(profile, page + 1).open(profile.getPlayer());
                });

        File worldContainer = Bukkit.getServer().getWorldContainer();

        List<String> mapIdentifiers = new ArrayList<>();
        // Check if the directory exists and is actually a directory
        if (worldContainer.isDirectory()) {
            // List all files in the directory
            File[] files = worldContainer.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Check if the file is a directory (a world folder)
                    if (file.isDirectory()) {
                        // Print the name of the world folder
                        mapIdentifiers.add(file.getName());
                    }
                }
            } else {
                Bukkit.getLogger().warning("No files found in the world container directory!");
            }
        } else {
            Bukkit.getLogger().warning("World container is not a valid directory!");
        }

        for (int i = 9; i < 54; i++) {

            int mapIndex = (i + ((54 - 9) * (page - 1))) - 9;

            if (mapIndex >= mapIdentifiers.size()) {
                break;
            }

            String identifier = mapIdentifiers.get(mapIndex);

            setItem(i, new ItemBuilder(Material.EMPTY_MAP)
                            .name("&a" + identifier)
                            .build(),
                    e -> {
                        profile.getPlayer().performCommand("map " + identifier);

                    });

        }
    }
}
