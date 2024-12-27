package club.mcgamer.xime.listener.hub;

import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.map.MapData;
import club.mcgamer.xime.server.event.ServerLoadEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;

public class HubLoadListener extends IListener {

    @EventHandler
    private void onHubLoad(ServerLoadEvent event) {
        if (event.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) event.getServerable();

            MapData mapData = MapData.load(HubServerable.MAP_NAME);

            serverable.setSpawnLocation(mapData.getCenterLocation().toBukkit(event.getWorld()));
            serverable.getSpawnLocation().setYaw(-90);
        }
    }

}

