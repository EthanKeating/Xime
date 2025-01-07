package club.mcgamer.xime.packet;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PacketHandler extends PacketListenerAbstract {

    private final XimePlugin plugin;

    public PacketHandler(XimePlugin plugin) {
        this.plugin = plugin;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();

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

//            JsonArray sampleArray = new JsonArray();
//
//            sampleArray.add(createLine("&e&lSGHQ Network"));
//            sampleArray.add(createLine(""));
//
//            sampleArray.add(createLine(" &7┃ &fThe SG Community Hub"));
//            sampleArray.add(createLine(""));
//
//            playersObject.add("sample", sampleArray);


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