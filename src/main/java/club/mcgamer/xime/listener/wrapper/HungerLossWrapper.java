package club.mcgamer.xime.listener.wrapper;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerHungerLossEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerLossWrapper extends IListener {

    @EventHandler
    private void onHungerLoss(FoodLevelChangeEvent event) {
        Profile profile = plugin.getProfileHandler().getProfile((Player) event.getEntity());

        if (profile.getServerable() == null)
            return;

        Bukkit.getPluginManager().callEvent(new ServerHungerLossEvent(
                profile,
                profile.getServerable(),
                event));
    }

}
