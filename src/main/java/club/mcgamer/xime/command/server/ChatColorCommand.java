package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        String prefix = profile.getServerable().getPrefix();

        if (!hasArgs(sender, args, 1)) {
            profile.sendMessage(prefix + "&fYou have the following options:");
            profile.sendMessage(prefix + "&a&&aa &c&&cc &e&&ee &9&&99 &b&&bb &f&&ff");
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
//                profile.getPlayerData().setch
                profile.sendMessage(prefix + "&fYour chat color now looks like " + TextUtil.translate(args[0]) + "this&8.");
                return true;
            default:
                profile.sendMessage(prefix + "&fYou have the following options:");
                profile.sendMessage(prefix + "&a&&aa &c&&cc &e&&ee &9&&99 &b&&bb &f&&ff");
        }
        return true;
    }

}
