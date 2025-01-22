package club.mcgamer.xime.listener;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.listener.bg.*;
import club.mcgamer.xime.listener.build.BuildItemInteractListener;
import club.mcgamer.xime.listener.build.BuildJoinListener;
import club.mcgamer.xime.listener.build.BuildLoadListener;
import club.mcgamer.xime.listener.hub.*;
import club.mcgamer.xime.listener.server.*;
import club.mcgamer.xime.listener.sg.*;
import club.mcgamer.xime.listener.sgmaker.SGMakerItemInteractListener;
import club.mcgamer.xime.listener.sgmaker.SGMakerJoinListener;
import club.mcgamer.xime.listener.sgmaker.SGMakerLoadListener;
import club.mcgamer.xime.listener.staff.StaffChatListener;
import club.mcgamer.xime.listener.staff.StaffDamageListener;
import club.mcgamer.xime.listener.staff.StaffInteractListener;
import club.mcgamer.xime.listener.staff.StaffOtherJoinListener;
import club.mcgamer.xime.listener.wrapper.*;

public class ListenerHandler {

    private final XimePlugin plugin;

    public ListenerHandler(XimePlugin plugin) {
        this.plugin = plugin;

        // Staff listeners
        new StaffOtherJoinListener();
        new StaffInteractListener();
        new StaffDamageListener();
        new StaffChatListener();

        //BG listeners
        new BGJoinListener();
        new BGInteractListener();
        new BGLoadListener();
        new BGBuildListener();
        new BGDamageListener();
        new BGQuitListener();
        new BGChatListener();
        new BGDropListener();

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
        new EnchantListener();

        //Wrappers, used to wrap server based events for ease of use
        new BlockModifyWrapper();
        new ItemInteractWrapper();
        new PlayerDamageWrapper();
        new HungerLossWrapper();
        new ItemDropWrapper();
        new WorldLoadWrapper();
        new PlayerChatWrapper();

        //SGMaker
        new SGMakerLoadListener();
        new SGMakerJoinListener();
        new SGMakerItemInteractListener();
    }

}
