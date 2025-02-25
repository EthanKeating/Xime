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
        String prefix = profile.getServerable().getPrefix();

        if (args.length == 0) {
            profile.sendMessage(String.format(prefix + "&fYour current ping is &6%s &fms.", PacketEvents.getAPI().getPlayerManager().getPing(player)));
            profile.sendMessage(prefix + "&fThe server is hosted in &aLondon, England");
            return true;
        }

        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;

        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (argumentProfile.getDisguiseData() != null) {
            if (!argumentProfile.getNameBypassDisguise().equalsIgnoreCase(argumentProfile.getName())) {
                if (argumentProfile.getNameBypassDisguise().equalsIgnoreCase(args[0])) {
                    isPlayer(sender, "......................");
                    return true;
                }
            }
        }
        profile.sendMessage(String.format(prefix + "&f%s&f's current ping is &6%s &fms.", argumentProfile.getDisplayName(), PacketEvents.getAPI().getPlayerManager().getPing(argumentPlayer)));
        profile.sendMessage(prefix + "&fThe server is hosted in &aLondon, England");

        return true;
    }
}