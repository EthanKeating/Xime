package club.mcgamer.xime.packet;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.command.CommandHandler;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.stream.Collectors;

public class PacketHandler extends PacketListenerAbstract {

    private final XimePlugin plugin;

    public static WrapperPlayServerPlayerInfo.PlayerData PLAYER_INFO;

    private String motd = "";

    public PacketHandler(XimePlugin plugin) {
        this.plugin = plugin;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getEventManager().registerListener(this);

        motd = TextUtil.translate(plugin.getLanguageHandler().getDefaultLanguage().getMotd());
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();

        if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage packet = new WrapperPlayClientChatMessage(event);

            String[] splitMessage = packet.getMessage().split(" ");
            Profile profile = plugin.getProfileHandler().getProfile(event.getUser().getUUID());

            if (splitMessage.length == 0) return;
            if (profile == null) return;

            event.setCancelled(plugin.getCommandHandler().onCommand(profile, splitMessage[0]));
        }

        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
            WrapperPlayClientTabComplete packet = new WrapperPlayClientTabComplete(event);

            //Bukkit.broadcastMessage("Command: " + packet.getText());
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        User user = event.getUser();

        if (event.getPacketType() == PacketType.Play.Server.TAB_COMPLETE) {
            WrapperPlayServerTabComplete packet = new WrapperPlayServerTabComplete(event);

            //Bukkit.broadcastMessage("Matches: " + packet.getCommandMatches()
                    //.stream().map(WrapperPlayServerTabComplete.CommandMatch::getText)
                    //.collect(Collectors.joining(", ")));
//
//            Bukkit.broadcastMessage("Range: " + packet.getCommandRange());
        }

        if (event.getPacketType() == PacketType.Status.Server.RESPONSE) {
            WrapperStatusServerResponse wrappedPacket = new WrapperStatusServerResponse(event);

            JsonObject object = wrappedPacket.getComponent();


//            if (plugin.getLanguageHandler() != null && plugin.getLanguageHandler().getDefaultLanguage() != null) {
//                motd = TextUtil.translate(plugin.getLanguageHandler().getDefaultLanguage().getMotd());
//            }
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


            if (plugin.getServerHandler().isWhitelisted())
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