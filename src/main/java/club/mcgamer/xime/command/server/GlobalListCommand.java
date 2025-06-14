package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GlobalListCommand extends XimeCommand {

    public GlobalListCommand() {
        super("globallist");
        this.description = "list players on the network";
        this.usageMessage = "/globallist";
        this.setAliases(Arrays.asList("glist"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (sender instanceof Player && !hasPermission(sender)) return true;

        String serverMessage = "&8[&3Xime&8] &8[&a%s&8] &fPlayers: &8[&6%s&8/&6%s&8]";
        String globalMessage = "&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers on the network.";

        plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> !serverable.getPlayerList().isEmpty())
                .forEach(serverable -> sender.sendMessage(
                        TextUtil.translate(String.format(serverMessage,
                                serverable,
                                serverable.getPlayerList().size(),
                                serverable.getMaxPlayers()))));

        sender.sendMessage(TextUtil.translate(String.format(globalMessage, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers())));

        return true;
    }

}
