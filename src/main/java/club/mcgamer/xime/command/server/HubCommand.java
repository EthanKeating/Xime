package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.TextUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class HubCommand extends XimeCommand {

    public HubCommand() {
        super("hub");
        this.description = "Return to the hub";
        this.usageMessage = "/hub [id]";
        this.setAliases(Arrays.asList("leave", "lobby"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (args.length > 0) {
            if (!StringUtils.isNumeric(args[0])) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
                return true;
            }

            int id = Integer.parseInt(args[0]);
            Optional<Serverable> serverableOptional = serverHandler.getServerList().stream()
                    .filter(serverable -> serverable instanceof HubServerable)
                    .filter(serverable -> serverable.getServerId() == id)
                    .findFirst();

            if (serverableOptional.isEmpty()) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
                return true;
            }

            Serverable serverable = serverableOptional.get();

            profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));
            Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
            return true;
        }

        Serverable serverable = serverHandler.getFallback();
        profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));
        Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
        return true;
    }

}
