package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerDropItemEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class BGInventoryListener extends IListener {

    @EventHandler
    public void onItemDrop(ServerDropItemEvent event){

        if (!(event.getServerable() instanceof BGServerable serverable)) return;

        event.getEvent().setCancelled(true);
    }



}
