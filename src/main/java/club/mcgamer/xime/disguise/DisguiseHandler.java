package club.mcgamer.xime.disguise;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.DisguiseUtil;
import club.mcgamer.xime.util.Skin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                profile,
                profile.getUuid(),
                randomName,
                skin)
        );
        profile.getDisguiseData().setMockData(PlayerData.createMock(profile));
        disguises.put(profile.getUuid(), profile.getDisguiseData());

//        profile.getUser().getProfile().setName(randomName);
//        profile.getPlayer().setDisplayName(profile.getName());

        DisguiseUtil.setSkin(profile, skin);
        DisguiseUtil.setName(profile, randomName);
        DisguiseUtil.updateToDisguise(profile);

        profile.sendMessage("&8[&6MCSG&8] &c&lWarning! &cThis command is logged.")
                .sendMessage("&8[&6MCSG&8] &cStaff can see your true username while using this command.")
                .sendMessage("&8[&6MCSG&8] &fYou now appear as " + profile.getDisplayName() + "&8.")
                .sendMessage("&8[&6MCSG&8] &fTo undisguise, use &8[&e/undisguise&8]");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(plugin.getDataFolder().getAbsolutePath(), "disguises").toFile(), true))) {
            writer.write(String.format("%s '%s' has disguised as '%s'",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    profile.getNameBypassDisguise(),
                    randomName));
            writer.newLine();
        } catch (IOException e) {
        }

    }

    @SneakyThrows
    public void undisguise(Profile profile) {
        //update real player stats
        profile.setDisguiseData(null);

//        profile.getUser().getProfile().setName(profile.getName());
//        profile.getPlayer().setDisplayName(profile.getName());

        DisguiseUtil.setSkin(profile, profile.getSkin());
        DisguiseUtil.setName(profile, profile.getName());
        DisguiseUtil.updateBackToNormal(profile);
        disguises.remove(profile.getUuid());

        profile.sendMessage("&8[&6MCSG&8] &fYou have been undisguised");
    }

}
