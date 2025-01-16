package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StaffListCommand extends XimeCommand {

    public StaffListCommand() {
        super("stafflist");
        this.description = "list staff on the network";
        this.usageMessage = "/stafflist";
        this.setAliases(Arrays.asList("staffonline", "sl", "so"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (sender instanceof Player && !hasPermission(sender)) return true;

        String globalMessage = "&8[&3Xime&8] &fThere are &8[&6%s&8] &fstaff on the network.";
        String serverMessage = "&8[&3Xime&8] &8";

        sender.sendMessage(TextUtil.translate(serverMessage + plugin.getProfileHandler().getProfiles().stream().filter(profile -> profile.getPlayer().hasPermission("xime.staff"))
                .sorted((p1, p2) -> Integer.compare(
                        plugin.getRankHandler().getRankList().indexOf(p1.getRank()),
                        plugin.getRankHandler().getRankList().indexOf(p2.getRank())
                ))
                .map(Profile::getDisplayNameBypassDisguise)
                .collect(Collectors.joining("&8, "))));


        sender.sendMessage(TextUtil.translate(String.format(globalMessage, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers())));

        return true;
    }

}
