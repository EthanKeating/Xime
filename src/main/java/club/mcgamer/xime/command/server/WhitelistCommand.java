package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.util.TextUtil;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class WhitelistCommand extends XimeCommand {

    public WhitelistCommand() {
        super("whitelist");
        this.description = "Toggle the server whitelist";
        this.usageMessage = "/whitelist <on/off/player>";
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
            Bukkit.setWhitelist(true);
            Bukkit.reloadWhitelist();
            return true;
        }
        else if(args[0].equalsIgnoreCase("off")) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &fThe server has been &aunwhitelisted."));
            Bukkit.setWhitelist(false);
            Bukkit.reloadWhitelist();
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (offlinePlayer == null) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cCould not find an offline player named " + args[0]));
            return true;
        }


        Bukkit.getWhitelistedPlayers().add(offlinePlayer);
        Bukkit.reloadWhitelist();
        sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &f%s &fhas been added to the &bwhitelist", "&2" + offlinePlayer.getName())));

        return true;
    }
}