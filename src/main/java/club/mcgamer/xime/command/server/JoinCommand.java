package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class JoinCommand extends XimeCommand {

    public JoinCommand() {
        super("join");
        this.description = "Join a server";
        this.usageMessage = "/join <game> [id]";
        this.setAliases(new ArrayList<>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if (!args[0].equalsIgnoreCase("SG")) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cUsage: <game> must be a valid game."));
            return true;
        }

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (args.length > 1) {

            if (!StringUtils.isNumeric(args[1])) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
                return true;
            }

            int id = Integer.parseInt(args[1]);
            Optional<Serverable> serverableOptional = serverHandler.getServerList().stream()
                    .filter(serverable -> serverable instanceof SGServerable)
                    .filter(serverable -> !(serverable instanceof SGMakerServerable))
                    .filter(serverable -> serverable.getServerId() == id)
                    .findFirst();

            if (!serverableOptional.isPresent()) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
                return true;
            }

            Serverable serverable = serverableOptional.get();
            profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));
            Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);

            return true;
        }

        // Sort by player count in ascending order
        // Sort by player count in ascending order
        // Then sort by game state in ascending order
        Optional<SGServerable> serverableOptional = plugin.getServerHandler().getServerList().stream()
                .filter(serverable -> serverable instanceof SGServerable)
                .filter(serverable -> !(serverable instanceof SGMakerServerable))
                .filter(serverable -> serverable.getPlayerList().size() < serverable.getMaxPlayers())
                .map(serverable -> (SGServerable) serverable).max(Comparator
                        .comparingInt((SGServerable serverable) -> serverable.getGameState().ordinal())
                        .reversed()// Sort by player count in ascending order
                        .thenComparing((SGServerable serverable) -> serverable.getGameState().ordinal()));

        if (!serverableOptional.isPresent()) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
            return true;
        }

        Serverable serverable = serverableOptional.get();
        profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));
        Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
        return true;
    }

}
