package club.mcgamer.xime.map;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.map.impl.MapData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class MapHandler {

    private final XimePlugin plugin;

    private HashMap<String, MapData> mapPool = new HashMap<>();
    private List<String> activeMaps = new ArrayList<>();

    public MapHandler(XimePlugin plugin) {
        this.plugin = plugin;

        load();
    }

    //Also called on Xime reload
    public void load() {
        for(String mapIdentifier : plugin.getConfig().getStringList("maps"))
            mapPool.put(mapIdentifier, MapData.load(mapIdentifier));

        activeMaps.addAll(plugin.getConfig().getStringList("activemaps"));
    }

}
