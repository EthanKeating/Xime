package club.mcgamer.xime.listener.bg;

import club.mcgamer.xime.animation.TextShineAnimation;
import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.data.entities.PlayerData;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.event.ServerJoinEvent;
import club.mcgamer.xime.util.IListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BGJoinListener extends IListener {

    @EventHandler
    private void onBGJoin(ServerJoinEvent event) {
        if (!(event.getServerable() instanceof BGServerable serverable)) return;

        Profile profile = event.getProfile();
        PlayerData playerData = profile.getPlayerData();

        serverable.setWaiting(profile);

        new TextShineAnimation(plugin, profile, "You joined Battlegrounds " + serverable.getServerId());

        if (!playerData.isSilentJoin())
            serverable.announceRaw(String.format("&2%s &6has joined&8.", profile.getDisplayName()));
    }

}
