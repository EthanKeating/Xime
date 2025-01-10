package club.mcgamer.xime;

import club.mcgamer.xime.command.CommandHandler;
import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.design.DesignHandler;
import club.mcgamer.xime.disguise.DisguiseHandler;
import club.mcgamer.xime.listener.ListenerHandler;
import club.mcgamer.xime.menu.MenuHandler;
import club.mcgamer.xime.packet.PacketHandler;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.world.WorldHandler;
import com.github.retrooper.packetevents.PacketEvents;
import com.grinderwolf.swm.api.SlimePlugin;
import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.combat.CombatModule;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class XimePlugin extends JavaPlugin {

    private ServerHandler serverHandler;
    private ProfileHandler profileHandler;
    private DesignHandler designHandler;
    private PacketHandler packetHandler;
    private WorldHandler worldHandler;
    private CommandHandler commandHandler;
    private DisguiseHandler disguiseHandler;
    private MenuHandler menuHandler;
    private RankHandler rankHandler;
    private DataHandler dataHandler;
    private ListenerHandler listenerHandler;
    private SlimePlugin slimePlugin;

    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().checkForUpdates(false).bStats(false);
        PacketEvents.getAPI().load();
    }

    public void onEnable() {
        slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        serverHandler = new ServerHandler(this);
        profileHandler = new ProfileHandler(this);
        designHandler = new DesignHandler(this);
        packetHandler = new PacketHandler(this);
        worldHandler = new WorldHandler(this);
        commandHandler = new CommandHandler(this);
        disguiseHandler = new DisguiseHandler(this);
        rankHandler = new RankHandler(this);
        dataHandler = new DataHandler(this);
        menuHandler = new MenuHandler(this);
        listenerHandler = new ListenerHandler(this);

        CombatModule combatModule = Apollo.getModuleManager().getModule(CombatModule.class);
        combatModule.getOptions().set(CombatModule.DISABLE_MISS_PENALTY, true);
    }

    public void onDisable() {

    }

}
