package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PingCommand extends XimeCommand {

    public PingCommand() {
        super("ping");
        this.description = "Check your latency to the server";
        this.usageMessage = "/ping [player]";
        this.setAliases(new ArrayList<>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (args.length == 0) {
            profile.sendMessage(String.format("&8[&3Xime&8] &fYour current ping is &6%s &fms.", PacketEvents.getAPI().getPlayerManager().getPing(player)));
            profile.sendMessage("&8[&3Xime&8] &fThe server is hosted in &aFrankfurt, Germany");
            return true;
        }

        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;

        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);
        profile.sendMessage(String.format("&8[&3Xime&8] &f%s&f's current ping is &6%s &fms.", argumentProfile.getDisplayName(), PacketEvents.getAPI().getPlayerManager().getPing(argumentPlayer)));
        profile.sendMessage("&8[&3Xime&8] &fThe server is hosted in &aFrankfurt, Germany");

        return true;
    }
}