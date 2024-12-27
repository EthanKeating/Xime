package club.mcgamer.xime.disguise;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.DisguiseUtil;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.Skin;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class DisguiseHandler {

    @Getter
    private HashMap<UUID, DisguiseData> disguises = new HashMap<>();
    private final XimePlugin plugin;

    public DisguiseHandler(XimePlugin plugin) {
        this.plugin = plugin;
    }

    @SneakyThrows
    public void disguise(Profile profile) {
        String randomName = DisguiseUtil.getRandomName();
        Skin skin = DisguiseUtil.getRandomSkin();

        profile.setDisguiseData(new DisguiseData(
                profile.getUuid(),
                randomName,
                skin)
        );
        disguises.put(profile.getUuid(), profile.getDisguiseData());

        profile.getUser().getProfile().setName(randomName);
        profile.getPlayer().setDisplayName(profile.getName());

        DisguiseUtil.setSkin(profile, skin);
        DisguiseUtil.setName(profile, randomName);
        DisguiseUtil.update(profile);

        profile.sendMessage("&8[&6MCSG&8] &c&lWarning! &cThis command is logged.")
                .sendMessage("&8[&6MCSG&8] &cStaff can see your true username while using this command.");
        profile.sendMessage("&8[&6MCSG&8] &fYou now appear as " + profile.getDisplayName() + "&8.");
        profile.sendMessage("&8[&6MCSG&8] &fTo undisguise, use &8[&e/undisguise&8]");
    }

    @SneakyThrows
    public void undisguise(Profile profile) {
        //update real player stats
        profile.setDisguiseData(null);

        DisguiseUtil.setSkin(profile, profile.getSkin());
        DisguiseUtil.setName(profile, profile.getName());
        DisguiseUtil.update(profile);
        profile.getPlayer().setDisplayName(profile.getName());
        profile.getUser().getProfile().setName(profile.getName());
        disguises.remove(profile.getUuid());

        profile.sendMessage("&8[&6MCSG&8] &fYou have been undisguised");
    }

}
