package club.mcgamer.xime.listener;

import club.mcgamer.xime.listener.build.BuildItemInteractListener;
import club.mcgamer.xime.listener.build.BuildJoinListener;
import club.mcgamer.xime.listener.build.BuildLoadListener;
import club.mcgamer.xime.listener.hub.*;
import club.mcgamer.xime.listener.server.*;
import club.mcgamer.xime.listener.sg.*;
import club.mcgamer.xime.listener.wrapper.*;

public class ListenerHandler {

    public ListenerHandler() {

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
        new HubChatListener();

        //Build listeners
        new BuildJoinListener();
        new BuildLoadListener();
        new BuildItemInteractListener();

        //General server listeners
        new PlayerJoinListener();
        new PlayerQuitListener();
        new PlayerLoginListener();
        new WorldCleanListener();
        new WeatherChangeListener();
        new FishingRodListener();

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
