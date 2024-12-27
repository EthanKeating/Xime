package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WhitelistCommand extends XimeCommand {

    public WhitelistCommand() {
        super("whitelist");
        this.description = "Toggle the server whitelist";
        this.usageMessage = "/whitelist";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.whitelist");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);

        plugin.getServerHandler().setWhitelisted(!plugin.getServerHandler().isWhitelisted());
        final boolean isWhitelisted = plugin.getServerHandler().isWhitelisted();

        if (isWhitelisted)
            profile.sendMessage("&8[&3Xime&8] &bThe server has been &fwhitelisted.");
        else
            profile.sendMessage("&8[&3Xime&8] &bThe server has been &funwhitelisted.");

        return true;
    }
}