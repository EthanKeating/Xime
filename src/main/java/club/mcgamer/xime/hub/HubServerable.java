package club.mcgamer.xime.hub;

import club.mcgamer.xime.hub.design.bossbar.HubBossbarAdapter;
import club.mcgamer.xime.hub.design.sidebar.HubSidebarAdapter;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
public class HubServerable extends Serverable {

    public static final String MAP_NAME = "Hub";

    @Setter private Location spawnLocation;

    public HubServerable() {
        super();

        setSidebarAdapter(new HubSidebarAdapter());
        setBossbarAdapter(new HubBossbarAdapter());

        setWorld(toString(), MAP_NAME);
    }

    @Override
    public TemporaryData createTemporaryData() {
        return TemporaryData.DEFAULT;
    }

}
