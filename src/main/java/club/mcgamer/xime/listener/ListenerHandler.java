package club.mcgamer.xime.listener;

import club.mcgamer.xime.listener.build.BuildItemInteractListener;
import club.mcgamer.xime.listener.build.BuildJoinListener;
import club.mcgamer.xime.listener.build.BuildLoadListener;
import club.mcgamer.xime.listener.hub.*;
import club.mcgamer.xime.listener.server.PlayerJoinListener;
import club.mcgamer.xime.listener.server.PlayerQuitListener;
import club.mcgamer.xime.listener.server.WeatherChangeListener;
import club.mcgamer.xime.listener.server.WorldCleanListener;
import club.mcgamer.xime.listener.sg.*;
import club.mcgamer.xime.listener.wrapper.*;

public class ListenerHandler {

    public ListenerHandler() {
        new PlayerJoinListener();
        new PlayerQuitListener();
        new WorldCleanListener();
        new WeatherChangeListener();

        //SG listeners
        new SGJoinListener();
        new SGQuitListener();
        new SGLoadListener();
        new SGMoveListener();
        new SGDamageListener();
        new SGInteractListener();
        new SGBuildListener();
        new SGDropListener();
        new SGVehicleListener();
        new SGChatListener();

        //Hub listeners
        new HubJoinListener();
        new HubItemInteractListener();
        new HubBreakBlockListener();
        new HubPlaceBlockListener();
        new HubDamageListener();
        new HubHungerLossListener();
        new HubDropItemListener();
        new HubLoadListener();

        //Build listeners
        new BuildJoinListener();
        new BuildLoadListener();
        new BuildItemInteractListener();

        //Wrappers, used to wrap server based events for ease of use
        new BlockModifyWrapper();
        new ItemInteractWrapper();
        new PlayerDamageWrapper();
        new HungerLossWrapper();
        new ItemDropWrapper();
        new WorldLoadWrapper();
        new PlayerChatWrapper();
    }

}
