package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

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

        if (sender instanceof Player) {
            if (!hasPermission(sender))
                return true;
        }

        for(Serverable serverable : plugin.getServerHandler().getServerList()) {
            if (!serverable.getPlayerList().isEmpty()) {
                sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &8[&a%s&8] &fPlayers: &8[&6%s&8/&6%s&8]", serverable, serverable.getPlayerList().size(), serverable.getMaxPlayers())));
            }
        }
        sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers on the network.", Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers())));

        return true;
    }

}
