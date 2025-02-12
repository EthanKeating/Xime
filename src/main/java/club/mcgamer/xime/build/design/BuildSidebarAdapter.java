package club.mcgamer.xime.build.design;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.map.impl.MapData;
import club.mcgamer.xime.profile.Profile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuildSidebarAdapter extends SidebarAdapter {
    @Override
    public String getTitle(Profile profile) {
        return "&b&lMap Editor";
    }

    @Override
    public List<String> getLines(Profile profile) {
        if (!(profile.getServerable() instanceof BuildServerable serverable))
            return Collections.emptyList();

        MapData mapData = serverable.getMapData();

        return Arrays.asList(
                "&6&l» Map Info",
                "&f" + (mapData.getMapName().length() > 16 ? (mapData.getMapName().substring(0, 16) + "...") : mapData.getMapName()),
                "&f" + (mapData.getMapAuthor().length() > 16 ? (mapData.getMapAuthor().substring(0, 16) + "...") : mapData.getMapAuthor()),
                "&f" + (mapData.getMapLink().length() > 16 ? (mapData.getMapLink().substring(0, 16) + "...") : mapData.getMapLink()),
                "",
                "&6&l» Location Data",
                String.format("&fCenter: &a%s, %s, &a%s", (int)mapData.getCenterLocation().getX(), (int)mapData.getCenterLocation().getY(), (int)mapData.getCenterLocation().getZ()),
                "&fDM Center: " + (mapData.getDmCenterLocation() == null ? "&cUnset" : String.format("&a%s, %s, %s", (int) mapData.getDmCenterLocation().getX(), (int) mapData.getDmCenterLocation().getY(), (int) mapData.getDmCenterLocation().getZ())),
                "&fSpectator: " + (mapData.getSpectateLocation() == null ? "&cUnset" : String.format("&a%s, %s, %s", (int) mapData.getSpectateLocation().getX(), (int) mapData.getSpectateLocation().getY(), (int) mapData.getSpectateLocation().getZ())),
                "",
                "&6&l» Spawn Data",
                "Spawns: " + ((mapData.getSpawnLocations() == null || mapData.getSpawnLocations().isEmpty()) ? "&c0" : ("&a" + mapData.getSpawnLocations().size())),
                "Dm Spawns: " + ((mapData.getDmLocations() == null || mapData.getDmLocations().isEmpty()) ? "&c0" : ("&a" + mapData.getDmLocations().size()))
                );
    }

    @Override
    public void tick() {}

    @Override
    public int getUpdateRate() {
        return 10;
    }
}
