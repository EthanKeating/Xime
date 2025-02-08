package club.mcgamer.xime.util;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@UtilityClass
public class DisguiseUtil {

    @SneakyThrows
    public void setSkin(Profile profile, Skin skin) {
        GameProfile gameProfile = (GameProfile) SpigotReflectionUtil.getGameProfile(profile.getPlayer());
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
    }

    @SneakyThrows
    public void setSkin(Profile profile, UUID uuidOfSkinOwner) {
        GameProfile gameProfile = (GameProfile) SpigotReflectionUtil.getGameProfile(profile.getPlayer());

        //TODO: RUN ASYNC
        URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidOfSkinOwner + "?unsigned=false");
        InputStreamReader reader = new InputStreamReader(url.openStream());
        JsonObject properties = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
        String texture = properties.get("value").getAsString();
        String signature = properties.get("signature").getAsString();
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));
    }

    public Skin getSkin(Player player) {
        GameProfile profile = ((CraftPlayer) player).getHandle().getProfile();
        if (profile.getProperties().get("textures").iterator().hasNext()) {
            Property property = profile.getProperties().get("textures").iterator().next();
            return new Skin(property.getValue(), property.getSignature());
        }
        return Skin.DEAD;
    }

    @SneakyThrows
    public void setName(Profile profile, String name) {
        GameProfile gameProfile = (GameProfile) SpigotReflectionUtil.getGameProfile(profile.getPlayer());

        Field nameField = Reflection.getField(GameProfile.class, "name");
        nameField.set(gameProfile, name);
    }

    private void update(Profile profile) {
        Bukkit.getScheduler().runTask(profile.getPlugin(), () -> {
            Location startLoc = profile.getPlayer().getLocation();
//            profile.getServerable().getPlayerList().stream().map(Profile::getPlayer).forEach(loopPlayer -> {
//                loopPlayer.hidePlayer(profile.getPlayer());
//                loopPlayer.showPlayer(profile.getPlayer());
//            });
            profile.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            Bukkit.getScheduler().runTaskLater(profile.getPlugin(), () -> {profile.getPlayer().teleport(startLoc);}, 3);
        });
    }

    @SneakyThrows
    public Skin getRandomSkin() {
        File file = Paths.get(XimePlugin.getPlugin(XimePlugin.class).getDataFolder().getAbsolutePath(), "skins.txt").toFile();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        Random random = new Random();

        long fileLength = raf.length();
        long randomPosition = (long) (random.nextDouble() * fileLength);

        // Move to the random position
        raf.seek(randomPosition);

        // Read until the next full line
        raf.readLine(); // Skip the potentially incomplete line
        String line = raf.readLine(); // Read the next full line

        if (line == null) { // If EOF, read from beginning
            raf.seek(0);
            line = raf.readLine();
        }

        String[] parts = line.split("NEWLINE");

        return new Skin(parts[0], parts[1]);
    }

    @SneakyThrows
    public String getRandomName() {
        File file = Paths.get(XimePlugin.getPlugin(XimePlugin.class).getDataFolder().getAbsolutePath(), "names.txt").toFile();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        Random random = new Random();

        long fileLength = raf.length();
        long randomPosition = (long) (random.nextDouble() * fileLength);

        // Move to the random position
        raf.seek(randomPosition);

        // Read until the next full line
        raf.readLine(); // Skip the potentially incomplete line
        String line = raf.readLine(); // Read the next full line

        if (line == null) { // If EOF, read from beginning
            raf.seek(0);
            line = raf.readLine();
        }

        raf.close();
        return line;
    }

    public void updateToDisguise(Profile profile) {
        GameProfile gameProfile = (GameProfile) SpigotReflectionUtil.getGameProfile(profile.getPlayer());

        WrapperPlayServerPlayerInfo addPlayer = new WrapperPlayServerPlayerInfo(
                WrapperPlayServerPlayerInfo.Action.ADD_PLAYER,
                new WrapperPlayServerPlayerInfo.PlayerData(
                        null,
                        new UserProfile(gameProfile.getId(),
                                profile.getName(),
                                SpigotReflectionUtil.getUserProfile(profile.getPlayer())),
                        GameMode.SURVIVAL,
                        0
                )
        );


        update(profile);
        profile.getUser().sendPacket(addPlayer);
    }

    @SneakyThrows
    public void loadSkins() {
        List<String> lines = Files.readAllLines(Paths.get(XimePlugin.getPlugin(XimePlugin.class).getDataFolder().getAbsolutePath(), "uuids.txt"));
        List<String> skins = new ArrayList<>();

        for(String uuid : lines) {
            UUID newUUID = UUID.fromString(
                    uuid
                            .replaceFirst(
                                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                            )
            );
            try {
                Skin skin = new Skin(newUUID);
                skins.add(skin.getValue() + "\\n" + skin.getSignature());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Files.write(Paths.get(XimePlugin.getPlugin(XimePlugin.class).getDataFolder().getAbsolutePath(), "skins.txt"), skins);
    }

    @SneakyThrows
    public void updateBackToNormal(Profile profile) {
        GameProfile gameProfile = (GameProfile) SpigotReflectionUtil.getGameProfile(profile.getPlayer());

        WrapperPlayServerPlayerInfo removePlayer = new WrapperPlayServerPlayerInfo(
                WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER,
                new WrapperPlayServerPlayerInfo.PlayerData(
                        null,
                        new UserProfile(gameProfile.getId(),
                                profile.getName(),
                                SpigotReflectionUtil.getUserProfile(profile.getPlayer())),
                        GameMode.SURVIVAL,
                        0
                )
        );
        WrapperPlayServerPlayerInfo addPlayer = new WrapperPlayServerPlayerInfo(
                WrapperPlayServerPlayerInfo.Action.ADD_PLAYER,
                new WrapperPlayServerPlayerInfo.PlayerData(
                        null,
                        new UserProfile(gameProfile.getId(),
                                profile.getNameBypassDisguise(),
                                SpigotReflectionUtil.getUserProfile(profile.getPlayer())),
                        GameMode.SURVIVAL,
                        0
                        )
        );

        profile.getUser().sendPacket(removePlayer);
        update(profile);
        profile.getUser().sendPacket(addPlayer);
    }

}
