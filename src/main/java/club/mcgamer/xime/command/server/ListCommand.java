package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.stream.Collectors;

public class ListCommand extends XimeCommand {

    public ListCommand() {
        super("list");
        this.description = "list players on your server";
        this.usageMessage = "/list";
        this.setAliases(Collections.singletonList("l"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof HubServerable || profile.getServerable() instanceof BGServerable) {
            Serverable serverable = profile.getServerable();

            profile.sendMessage(String.format("&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers online&8.", serverable.getPlayerList().size(), serverable.getMaxPlayers()));
            profile.sendMessage("&8- &f&lPlayers: &f" + serverable.getPlayerList().stream()
                    .map(loopProfile -> profile.getPlayer().hasPermission("xime.staff")
                            && loopProfile.getDisguiseData() != null
                            ? TextUtil.translate(String.format("%s&8(&f%s&8)",
                                profile.getDisplayName(),
                                profile.getDisplayNameBypassDisguise()))
                            : TextUtil.translate(loopProfile.getDisplayName()))
                    .collect(Collectors.joining("&8, &f")));
            return true;
        }

        if (profile.getServerable() instanceof SGServerable serverable) {

            profile.sendMessage(String.format("&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers online&8.", serverable.getPlayerList().size(), serverable.getMaxPlayers()));

            if(serverable.getGameState() == GameState.LOBBY) {
                profile.sendMessage("&8- &f&lPlaying: &f" + serverable.getPlayerList().stream()
                        .map(loopProfile -> profile.getPlayer().hasPermission("xime.staff") && loopProfile.getDisguiseData() != null ? TextUtil.translate(loopProfile.getDisplayName() + "&8(" + loopProfile.getDisplayNameBypassDisguise() + "&8)") : TextUtil.translate(loopProfile.getDisplayName()))
                        .collect(Collectors.joining("&8, &f")));
                return true;
            }

            profile.sendMessage("&8- &f&lPlaying: &f" + serverable.getTributeList().stream()
                    .map(loopProfile -> profile.getPlayer().hasPermission("xime.staff") && loopProfile.getDisguiseData() != null ? TextUtil.translate(loopProfile.getDisplayName() + "&8(" + loopProfile.getDisplayNameBypassDisguise() + "&8)") : TextUtil.translate(loopProfile.getDisplayName()))
                    .collect(Collectors.joining("&8, &f")));
            profile.sendMessage("&8- &f&lWatching: &f" + serverable.getSpectatorList().stream()
                    .map(loopProfile -> profile.getPlayer().hasPermission("xime.staff") && loopProfile.getDisguiseData() != null ? TextUtil.translate(loopProfile.getDisplayName() + "&8(" + loopProfile.getDisplayNameBypassDisguise() + "&8)") : TextUtil.translate(loopProfile.getDisplayName()))
                    .collect(Collectors.joining("&8, &f")));
        }

        return true;
    }

}
