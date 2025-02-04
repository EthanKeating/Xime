package club.mcgamer.xime.staff;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.staff.design.sidebar.StaffSidebarAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffServerable extends Serverable {

    public StaffServerable() {
        super();

        setMaxPlayers(-1);
        setSidebarAdapter(new StaffSidebarAdapter());
        overrideWorld(Bukkit.getWorlds().get(0));
    }

    @Override
    public TemporaryData createTemporaryData() {
        return null;
    }

    public void add(Profile profile) {
        Player player = profile.getPlayer();

        if (profile.getServerable() == this) {
            profile.sendMessage("&cYou are already connected to that server.");
            return;
        }

        if (profile.getServerable() != null)
            profile.getServerable().remove(profile);

        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.hidePlayer(player);
            player.showPlayer(loopPlayer);
        });

        getPlayerList().remove(profile);
        getPlayerList().add(profile);
        profile.setServerable(this);
        profile.setTemporaryData(createTemporaryData());
        Bukkit.getPluginManager().callEvent(new ServerJoinEvent(profile, profile.getServerable()));
    }
}
