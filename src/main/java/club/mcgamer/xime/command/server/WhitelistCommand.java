package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class WhitelistCommand extends XimeCommand {

    public WhitelistCommand() {
        super("whitelist");
        this.description = "Toggle the server whitelist";
        this.usageMessage = "/whitelist <on/off>";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!hasPermission(sender)) return true;
        if (!hasArgs(sender, args, 1)) {

            sender.sendMessage(Bukkit.getWhitelistedPlayers()
                    .stream()
                    .map(OfflinePlayer::getName)
                    .collect(Collectors.joining(", ")));
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &fThe server has been &cwhitelisted."));
            plugin.getServerHandler().setWhitelisted(true);
            return true;
        }
        else if(args[0].equalsIgnoreCase("off")) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &fThe server has been &aunwhitelisted."));
            plugin.getServerHandler().setWhitelisted(false);
            return true;
        }
        return true;
    }
}