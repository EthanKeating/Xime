package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.soap.Text;
import java.util.Arrays;

public class ChatColorCommand extends XimeCommand {

    public ChatColorCommand() {
        super("chatcolor");
        this.description = "Change your chat color";
        this.usageMessage = "/chatcolor <color>";
        this.setAliases(Arrays.asList("chatcolour", "colour", "color", "cc"));
        setPermission("xime.platinum");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!hasArgs(sender, args, 1)) {
            profile.sendMessage("&8[&3Xime&8] &fYou have the following options:");
            profile.sendMessage("&8[&3Xime&8] &a&&aa &c&&cc &e&&ee &9&&99 &b&&bb &f&&ff");
            return true;
        }

        switch (args[0]) {
            case "&a":
            case "&c":
            case "&e":
            case "&9":
            case "&f":
            case "&b":
                profile.setChatColor(TextUtil.translate(args[0]));
                profile.sendMessage("&8[&3Xime&8] &fYour chat color now looks like " + TextUtil.translate(args[0]) + "this&8.");
                return true;
            default:
                player.sendMessage(TextUtil.translate("&8[&3Xime&8] &fYou have the following options:"));
                profile.sendMessage("&8[&3Xime&8] &a&&aa &c&&cc &e&&ee &9&&99 &b&&bb &f&&ff");
        }

        return true;
    }

}
