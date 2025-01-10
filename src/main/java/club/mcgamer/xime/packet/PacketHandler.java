package club.mcgamer.xime.packet;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.CommandHandler;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.BlockUtil;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.retrooper.packetevents.adventure.serializer.json.JSONComponentSerializer;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketHandler extends PacketListenerAbstract {

    private final XimePlugin plugin;

    public static WrapperPlayServerPlayerInfo.PlayerData PLAYER_INFO;

    public PacketHandler(XimePlugin plugin) {
        this.plugin = plugin;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();

        if(event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {

            WrapperPlayClientChatMessage packet = new WrapperPlayClientChatMessage(event);

            CommandHandler commandHandler = plugin.getCommandHandler();
            Profile profile = plugin.getProfileHandler().getProfile(event.getUser().getUUID());
            String command = packet.getMessage();

            event.setCancelled(commandHandler.onCommand(profile, command.toLowerCase()));

            //Bukkit.broadcastMessage(packet.getMessage());
        }

    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();

        if (event.getPacketType() == PacketType.Play.Server.SPAWN_EXPERIENCE_ORB) {
            event.setCancelled(true);
        }

//        if (event.getPacketType() == PacketType.Play.Server.DESTROY_ENTITIES) {
//
//            if (user.getClientVersion().isNewerThan(ClientVersion.V_1_7_10))
//                return;
//
//            WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(event);
//
//            List<Integer> entityIdList = new ArrayList<>();
//
//            for (int entityId : packet.getEntityIds()) {
//                Entity entity = SpigotReflectionUtil.getEntityById(entityId);
//
//                if (entity instanceof Player) {
//                    entityIdList.add(entityId);
//                    entityIdList.add(entityId + 1000000);
//                }
//            }
//            packet.setEntityIds(entityIdList.stream().mapToInt(i->i).toArray());
//        }
//
//        if (event.getPacketType() == PacketType.Play.Server.SPAWN_PLAYER) {
//
//            if (user.getClientVersion().isNewerThan(ClientVersion.V_1_7_10))
//                return;
//
//            WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(event);
//
//            Entity entity = SpigotReflectionUtil.getEntityById(packet.getEntityId());
//
//            if (entity instanceof Player) {
//                Player viewedPlayer = (Player) entity;
//                if (true) {
//                    user.sendPacketSilently(new WrapperPlayServerSpawnLivingEntity(
//                            packet.getEntityId() + 1000000,
//                            UUID.randomUUID(),
//                            EntityTypes.getByLegacyId(ClientVersion.V_1_8, 65),
//                            new Vector3d(viewedPlayer.getLocation().getX(), 1000, viewedPlayer.getLocation().getY()),
//                            0,
//                            0,
//                            0,
//                            new Vector3d(0.0, 0.0, 0.0),
//                            Collections.singletonList(new EntityData(0, EntityDataTypes.BYTE, (byte) 0x20))
//                    ));
//
//
//                }
//
//            }
//
//        }

        if (event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            WrapperPlayServerTabComplete packet = new WrapperPlayServerTabComplete(event);

            CommandHandler commandHandler = plugin.getCommandHandler();
            Profile profile = plugin.getProfileHandler().getProfile(event.getUser().getUUID());

            commandHandler.onTabComplete(profile, packet.getCommandMatches());
        }

        if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
//            WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
//
//            List<Object> objectList = Arrays.asList(
//                    joinGame.getEntityId(),
//                    joinGame.isHardcore(),
//                    joinGame.getGameMode(),
//                    joinGame.getPreviousGameMode(),
//
//                    joinGame.getWorldNames(),
//                    joinGame.getDimensionCodec(),
//                    joinGame.getDimensionTypeRef(),
//                    joinGame.getDifficulty(),
//                    joinGame.getHashedSeed(),
//                    joinGame.getWorldName(),
//                    joinGame.getMaxPlayers(),
//                    joinGame.getViewDistance(),
//                    joinGame.getSimulationDistance(),
//                    joinGame.isReducedDebugInfo(),
//                    joinGame.isRespawnScreenEnabled(),
//                    joinGame.isLimitedCrafting(),
//                    joinGame.isDebug(),
//                    joinGame.isFlat(),
//                    joinGame.getLastDeathPosition(),
//                    joinGame.getPortalCooldown(),
//                    joinGame.getSeaLevel(),
//                    joinGame.isEnforcesSecureChat()
//            );
//
//            objectList.forEach(object -> {
//                if (object instanceof List) {
//                    ((ArrayList) object).forEach(object2 -> user.sendMessage("   " + object2.toString()));
//                } else {
//                    user.sendMessage(object.toString());
//                }
//            });
        }

        if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
//            WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
//            WrapperPlayServerJoinGame join = new WrapperPlayServerJoinGame(
        }

        if (event.getPacketType() == PacketType.Status.Server.RESPONSE) {
            WrapperStatusServerResponse wrappedPacket = new WrapperStatusServerResponse(event);

            JsonObject object = wrappedPacket.getComponent();

            String motd =
                    "&8[&eMCGamer&8] &6&l1.7 &7& &6&l1.8 &2Alpha Wave &c#5.0.0\n" +
                            "&4Full recode in progress";

            object.addProperty("description", TextUtil.translate(motd));

//            JsonObject versionObject = wrappedPacket.getComponent().getAsJsonObject("version");
//            versionObject.addProperty("name", "1.7 & 1.8");

            JsonObject versionObject = wrappedPacket.getComponent().getAsJsonObject("version");
            versionObject.addProperty("name", "Whitelisted");
            versionObject.addProperty("protocol", 1);

            JsonObject playersObject = wrappedPacket.getComponent().getAsJsonObject("players");
            playersObject.addProperty("max", 1000);
            //playersObject.addProperty("online", 153 + Bukkit.getOnlinePlayers().size());

            JsonArray sampleArray = new JsonArray();

            sampleArray.add(createLine("&8[&eMCGamer Network&8]"));
            sampleArray.add(createLine(""));

            playersObject.remove("sample");
            playersObject.add("sample", sampleArray);


/*            if (CommandLockdown.LOCKDOWN_ENABLED)
                object.add("version", versionObject);*/
            if (Bukkit.hasWhitelist())
                object.add("version", versionObject);
            object.add("players", playersObject);
            wrappedPacket.setComponent(object);

        }
    }

    public JsonObject createLine(String line) {
        JsonObject sampleObject1 = new JsonObject();

        sampleObject1.addProperty("id", UUID.randomUUID().toString());
        sampleObject1.addProperty("name", TextUtil.translate(line));

        return sampleObject1;
    }

}