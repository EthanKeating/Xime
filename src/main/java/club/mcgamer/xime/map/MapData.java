package club.mcgamer.xime.map;

import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.GsonBuilder;
import com.viaversion.viaversion.libs.gson.stream.JsonReader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MapData {

    private String mapName = "Default Map Name";
    private String mapAuthor = "Default Map Author";
    private String mapLink = "Default Map Link";

    private final List<MapLocation> tier1Locations = new ArrayList<>();
    private final List<MapLocation> tier2Locations = new ArrayList<>();
    private final List<MapLocation> spawnLocations = new ArrayList<>();
    private final List<MapLocation> dmLocations = new ArrayList<>();

    private MapLocation dmCenterLocation = null;
    private MapLocation centerLocation = null;

    @SneakyThrows
    public static void save(String mapName, MapData mapData) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        FileWriter gsonWriter = new FileWriter(Paths.get(Bukkit.getWorldContainer().toString(), mapName, "mapData.json").toFile());

        gson.toJson(mapData, gsonWriter);
        gsonWriter.flush();
        gsonWriter.close();
    }

    @SneakyThrows
    public static MapData load(String mapName) {
        Gson gson = new Gson();

        File file = Paths.get(Bukkit.getWorldContainer().toString(), mapName, "mapData.json").toFile();
        if (!file.exists())
            return new MapData();

        FileReader fileReader = new FileReader(file);
        JsonReader gsonReader = new JsonReader(fileReader);

        return gson.fromJson(gsonReader, MapData.class);
    }
}
