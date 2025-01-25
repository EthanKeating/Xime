package club.mcgamer.xime.listener.server;

import club.mcgamer.xime.util.IListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.weather.WeatherChangeEvent;


public class EnvironmentListener extends IListener {

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    private void onLeafDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }


}
