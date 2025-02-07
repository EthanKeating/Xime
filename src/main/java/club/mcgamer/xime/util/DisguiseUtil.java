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
    public Skin getRandomSkin() {
        return new Skin(randomSkins.get(random.nextInt(randomSkins.size())));
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
            profile.getServerable().getPlayerList().stream().map(Profile::getPlayer).forEach(loopPlayer -> {
                loopPlayer.hidePlayer(profile.getPlayer());
                loopPlayer.showPlayer(profile.getPlayer());
            });
            profile.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            profile.getPlayer().teleport(startLoc);
        });
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

    public String getRandomNameOld() {
        String randomName = "";

        randomName += words.get(random.nextInt(words.size())) + words.get(random.nextInt(words.size()));
        randomName += random.nextBoolean() ? "" : randomNumbers.get(random.nextInt(randomNumbers.size()));
        randomName = random.nextBoolean() ? randomName : TextUtil.toPascalCase(randomName);
        randomName = randomName.length() > 16 ? randomName.substring(0, 15) : randomName;

        return randomName;
    }

    public static final Random random = new Random();

    @Getter
    private static final List<UUID> randomSkins = Arrays.asList(
            UUID.fromString("ac81d1cd-fe47-40e1-bc6d-e359aa50fc36"),
            UUID.fromString("5f0898ae-3f2e-4c99-b0fc-ed4c7fecbdea"),
            UUID.fromString("004c5a47-cfb9-4a42-b5df-6acc916b51ba"),
            UUID.fromString("b4e1ad07-5ed3-43c8-8bad-24314792d606"),
            UUID.fromString("6dc5dc3d-3915-44f2-8fd0-2b4c06783887"),
            UUID.fromString("3805901c-27ad-4405-b90f-1877222fe7e4"),
            UUID.fromString("dfcc0109-ba1a-4320-a917-a8970407e64b"),
            UUID.fromString("767af5f5-451c-47d3-982a-0dbdb208ad7d"),
            UUID.fromString("edf87631-e5e9-46ba-bedf-ba09ce06ad74"),
            UUID.fromString("1f177fc6-e22f-4e0e-98d1-d978b944167a"),
            UUID.fromString("a03b0a22-6b26-4d85-a78e-2666362f4018"),
            UUID.fromString("e6e932bb-355d-4fa8-a022-8a93fd759ad3"),
            UUID.fromString("002d8498-b8b7-41dc-ba97-8cc9a0da2aed"),
            UUID.fromString("b178f2ff-7414-46a1-8f66-9037537e5842"),
            UUID.fromString("47996360-381a-491e-888e-45be42c9babc"),
            UUID.fromString("0bfc67e1-9632-4137-813b-df3dee6d581f"),
            UUID.fromString("4981f9f2-84f6-44be-92d8-3cce2cac32d6"),
            UUID.fromString("49a76802-e7ec-4539-9bf3-264f457b509b"),
            UUID.fromString("b1dd7620-cceb-4378-a1ec-21b3cc1f8def"),
            UUID.fromString("7c318e8b-54f9-461f-a1c5-be58a5ad2544"),
            UUID.fromString("244574fb-0207-4dbd-a282-d6d4dfe5c278")

    );

    private static final List<String> words = Arrays.asList(
            "blade", "arrow", "armor", "block", "fight", "build", "sword",
            "spawn", "smite", "haste", "punch", "sharp", "power", "flame",
            "depth", "boots", "rogue", "zombie", "spider", "creeper",
            "strike", "clutch", "splash", "damage", "shadow", "hunter",
            "archer", "miner", "ender", "portal", "beacon", "totem",
            "dragon", "fury", "ignite", "burst", "quake", "venom",
            "knock", "combo", "stealth", "freeze", "shield", "phantom",
            "speed", "ravage", "ranger", "vortex", "spark", "ignite",
            "throne", "battle", "flinch", "nether", "freeze", "shadow",
            "crusher", "sniper", "potion", "attack", "flurry", "storm",
            "skull", "ghost", "widow", "fang", "sprint", "charge",
            "smash", "beast", "chaos", "slayer", "strike", "savage",
            "golem", "flint", "brute", "reaper", "master", "venge",
            "spikes", "bounty", "shards", "wither", "legacy", "lancer",
            "bowman", "venin", "wrath", "poison", "reven", "knight",
            "hunter", "forge", "grind", "amber", "quartz", "miner",
            "thief", "steal", "grief", "havoc", "bandit", "cloak",
            "magic", "feast", "smelt", "forge", "vault", "realm",
            "realm", "spike", "slash", "clash", "elite", "toxic",
            "alpha", "omega", "gamer", "swift", "frost", "laser",
            "snipe", "focus", "endgame", "flinch", "valor", "strike",
            "beacon", "portal", "cursed", "sword", "armor", "spawn",
            "apple", "orange", "banana", "grape", "melon", "cherry", "peach",
            "lemon", "plum", "berry", "mango", "olive", "peanut", "raisin",
            "apricot", "fig", "date", "kiwi", "guava", "pear", "papaya",
            "nectar", "mulch", "spade", "folks", "hiker", "trails", "canyon",
            "fields", "rivers", "stream", "forest", "mount", "slope", "valley",
            "summer", "winter", "spring", "autumn", "harvest", "cloudy",
            "sunset", "breeze", "thaw", "drifts", "frozen", "icicle", "blossom",
            "ripple", "shades", "fences", "hedges", "greens", "willow",
            "clover", "orchid", "tulip", "cactus", "juniper", "heather",
            "violet", "poppy", "daffod", "mossy", "fungus", "birch", "spruce",
            "marsh", "cedar", "beech", "maple", "ash", "hickory", "locust",
            "oak", "elm", "alder", "poplar", "walnut", "yew", "cobweb",
            "stones", "pebbles", "garnet", "quartz", "jasper", "agate",
            "granite", "shale", "slate", "pyrite", "flint", "amber",
            "opal", "topaz", "silver", "golden", "bronze", "copper",
            "nickel", "plated", "zinc", "chrome", "pearly", "ribbon",
            "velvet", "satin", "lace", "woolen", "thread", "cotton",
            "yarn", "twine", "linen", "fabric", "canvas", "leather",
            "thread", "silken", "woven", "knots", "braids", "plaits",
            "beads", "cords", "loops", "straps", "belts", "shoes",
            "boots", "clogs", "sandals", "moccasin", "slipper", "anklet",
            "bracelet", "ring", "chain", "pendant", "necklace", "brooch",
            "earring", "choker", "torque", "crown", "tiara", "circlet",
            "medal", "plaque", "crest", "trophy", "shield", "staff",
            "wand", "rod", "scepter", "tablet", "scroll", "ribbon",
            "shadow", "blaze", "venom", "chaos", "knight", "raven",
            "hunter", "phantom", "cypher", "stealth", "reaper", "nova",
            "blitz", "storm", "viper", "pulse", "ghost", "valor",
            "dread", "wrath", "alpha", "omega", "sniper", "strike",
            "frost", "crash", "flint", "shock", "ember", "ignite",
            "sparx", "toxic", "clutch", "blade", "rogue", "arctic",
            "rider", "zephyr", "spark", "zeal", "throne", "cyborg",
            "crusher", "havoc", "savage", "xeno", "titan", "stark",
            "onyx", "slayer", "flare", "dash", "volt", "rage",
            "venin", "rift", "lyric", "glitch", "chaos", "lancer",
            "spectre", "fatal", "primal", "predator", "revolt", "razor",
            "archer", "raven", "bravo", "cobalt", "strike", "elite",
            "jester", "forge", "spike", "torque", "vault", "iron",
            "myth", "pyro", "chill", "volt", "swift", "vortex",
            "beast", "quake", "snipe", "apex", "pixel", "spawn",
            "storm", "flare", "fang", "ember", "dusk", "fury",
            "grimm", "scout", "wick", "night", "zenith", "rogue",
            "neon", "raze", "bolt", "thief", "inferno", "plasma",
            "chaotic", "drift", "karma", "legacy", "bound", "orbit",
            "whiz", "forge", "razor", "hero", "nova", "flux",
            "nova", "glare", "razor", "rider", "prime", "phantom",
            "venge", "snarl", "slash", "shard", "onyx", "nova"
            );
    private static final List<String> randomNumbers = Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "13", "16", "18",
            "22", "33", "44", "55", "69", "77", "88", "99",
            "101", "123", "321", "14", "15", "78", "87", "85", "1990", "1995",
            "34", "51", "2023", "11", "22", "33", "44", "555", "123", "321",
            "55", "007", "1337", "808", "909", "5", "7", "1", "9999", "2024",
            "77", "515", "818", "303", "606", "99", "8", "7", "03", "313",
            "007", "22", "2003", "250", "400", "750", "88", "99", "10"
    );

}
