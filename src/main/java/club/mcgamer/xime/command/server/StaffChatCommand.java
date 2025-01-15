package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.ReplyData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChatCommand extends XimeCommand {

    public StaffChatCommand() {
        super("staffchat");
        this.description = "send a private message";
        this.usageMessage = "/staffchat <message>";
        this.setAliases(Arrays.asList("sc"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        String message = "&8[&5STAFF&8] &f" + profile.getDisplayNameBypassDisguise() + "&8: &b" + String.join(" ", args);

        plugin.getProfileHandler().getProfiles().stream()
                .filter(loopProfile -> loopProfile.getPlayer().hasPermission(getPermission()))
                .forEach(loopProfile -> loopProfile.sendMessage(message));

        return true;
    }
}
