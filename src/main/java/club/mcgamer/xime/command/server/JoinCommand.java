package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.util.TextUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class JoinCommand extends XimeCommand {

    public JoinCommand() {
        super("join");
        this.description = "Join a server";
        this.usageMessage = "/join <game> [id]";
        this.setAliases(Arrays.asList("quickjoin"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if (!args[0].equalsIgnoreCase("SG") && !args[0].equalsIgnoreCase("BG") && !args[0].equalsIgnoreCase("SGMAKER") ) {
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
            Optional<Serverable> serverableOptional = Optional.empty();

            if (args[0].equalsIgnoreCase("SG")) {
                serverableOptional = serverHandler.getServerList().stream()
                        .filter(serverable -> serverable instanceof SGServerable)
                        .filter(serverable -> !(serverable instanceof SGMakerServerable))
                        .filter(serverable -> serverable.getServerId() == id)
                        .findFirst();
            } else if (args[0].equalsIgnoreCase("BG")) {
                serverableOptional = serverHandler.getServerList().stream()
                        .filter(serverable -> serverable instanceof BGServerable)
                        .filter(serverable -> serverable.getServerId() == id)
                        .findFirst();
            } else if (args[0].equalsIgnoreCase("SGMAKER")) {
                serverableOptional = serverHandler.getServerList().stream()
                        .filter(serverable -> serverable instanceof SGMakerServerable)
                        .filter(serverable -> serverable.getServerId() == id)
                        .findFirst();
            }

            if (serverableOptional.isEmpty()) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
                return true;
            }

            Serverable serverable = serverableOptional.get();
            profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));

            if (serverable instanceof SGMakerServerable makerServerable) {
                if (makerServerable.getPrivacyMode() == PrivacyMode.PRIVATE && makerServerable.getOwner() != profile && !makerServerable.isInvited(profile)) {
                    sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cYou do not have access to this server!"));
                    return true;
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
            } else {
                Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
            }

            return true;
        }

        Optional<Serverable> serverableOptional = Optional.empty();

        if (args[0].equalsIgnoreCase("SG")) {
            serverableOptional = plugin.getServerHandler().getServerList().stream()
                    .filter(serverable -> serverable instanceof SGServerable)
                    .filter(serverable -> !(serverable instanceof SGMakerServerable))
                    .filter(serverable -> serverable.getPlayerList().size() < serverable.getMaxPlayers())
                    .map(serverable -> (SGServerable) serverable).max(Comparator
                            .comparingInt((SGServerable serverable) -> serverable.getGameState().ordinal())
                            .reversed()// Sort by player count in ascending order
                            .thenComparing((SGServerable serverable) -> serverable.getGameState().ordinal()))
                    .map(serverable -> (Serverable) serverable);
        } else if (args[0].equalsIgnoreCase("BG")){
            serverableOptional = plugin.getServerHandler().getServerList().stream()
                    .filter(serverable -> serverable instanceof BGServerable)
                    .filter(serverable -> serverable.getPlayerList().size() < serverable.getMaxPlayers())
                    .map(serverable -> (BGServerable) serverable).max(Comparator
                            .comparingInt((BGServerable serverable) -> serverable.getPlayerList().size()))
                    .stream().map(serverable -> (Serverable) serverable).findFirst();

        }

        if (serverableOptional.isEmpty()) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThis server is offline or does not exist."));
            return true;
        }

        Serverable serverable = serverableOptional.get();
        profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to &2%s &6(EU)&f..", serverable.toString()));
        Bukkit.getScheduler().runTaskLater(plugin, () -> serverable.add(profile), 1);
        return true;
    }

}
