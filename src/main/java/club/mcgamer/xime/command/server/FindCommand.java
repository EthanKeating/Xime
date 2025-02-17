package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class FindCommand extends XimeCommand {

    public FindCommand() {
        super("find");
        this.description = "Find a player";
        this.usageMessage = "/find [player]";
        this.setAliases(Arrays.asList("locate"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        Player argumentPlayer = isPlayer(sender, args[0]);

        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        profile.sendMessage(String.format("&8[&3Xime&8] &b%s&b is on &8[&a%s&8]", argumentProfile.getDisplayName(), argumentProfile.getServerable()));

        return true;
    }
}
