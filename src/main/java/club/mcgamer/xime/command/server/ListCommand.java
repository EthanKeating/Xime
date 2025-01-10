package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ListCommand extends XimeCommand {

    public ListCommand() {
        super("list");
        this.description = "list players on your server";
        this.usageMessage = "/list";
        this.setAliases(Collections.emptyList());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof HubServerable) {
            HubServerable serverable = (HubServerable) profile.getServerable();

            profile.sendMessage(String.format("&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers online&8.", serverable.getPlayerList().size(), serverable.getMaxPlayers()));
        }

        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();

            profile.sendMessage(String.format("&8[&3Xime&8] &fThere are &8[&6%s&8/&6%s&8] &fplayers online&8.", serverable.getPlayerList().size(), serverable.getMaxPlayers()));


            if(serverable.getGameState() == GameState.LOBBY) {
                profile.sendMessage("&8- &f&lPlaying: &f" + serverable.getPlayerList().stream().map(Profile::getDisplayName).collect(Collectors.joining("&8, &f")));
            } else {
                profile.sendMessage("&8- &f&lPlaying: &f" + serverable.getTributeList().stream().map(Profile::getDisplayName).collect(Collectors.joining("&8, &f")));
                profile.sendMessage("&8- &f&lWatching: &f" + serverable.getSpectatorList().stream().map(Profile::getDisplayName).collect(Collectors.joining("&8, &f")));
            }

        }

        return true;
    }

}
