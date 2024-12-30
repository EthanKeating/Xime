package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.impl.GeneralData;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FlyCommand extends XimeCommand {

    public FlyCommand() {
        super("fly");
        this.description = "Fly in hubs and lobbies";
        this.usageMessage = "/fly";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.donator");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!hasPermission(sender)) return true;
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        GeneralData generalData = profile.getGeneralData();

        generalData.setCanFly(!generalData.isCanFly());

        if (profile.getServerable() instanceof SGServerable) {
            if (((SGServerable) profile.getServerable()).getGameState() != GameState.LOBBY) {
                profile.sendMessage("&8[&3Xime&8] &cYou cannot use this command right now.");
                return true;
            }
        }

        if (generalData.isCanFly())
            profile.sendMessage("&8[&3Xime&8] &fYou can now fly&8.");
        else
            profile.sendMessage("&8[&3Xime&8] &fYou can no longer fly&8.");

        player.setAllowFlight(generalData.isCanFly());

        return true;
    }
}