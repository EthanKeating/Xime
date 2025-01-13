package club.mcgamer.xime.listener.staff;

import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StaffOtherJoinListener extends IListener {

    @EventHandler
    private void onOtherPlayerJoinServer(ServerJoinEvent event) {

        List<StaffServerable> staffServerableList = plugin.getServerHandler().getServerList()
                .stream()
                .filter(server -> server instanceof StaffServerable)
                .map(server -> (StaffServerable)server)
                .collect(Collectors.toList());
        Serverable serverable = event.getServerable();

        if (serverable instanceof StaffServerable)
            return;

        Player otherPlayer = event.getProfile().getPlayer();

        staffServerableList.forEach(staffServerable -> staffServerable.getPlayerList().forEach(profile -> profile.getPlayer().showPlayer(otherPlayer)));

    }

}
