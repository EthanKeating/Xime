package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DisguiseCommand extends XimeCommand {

    public DisguiseCommand() {
        super("disguise");
        this.description = "Get a random disguise";
        this.usageMessage = "/disguise";
        this.setAliases(Arrays.asList("d"));
        setPermission("xime.diamond");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!(profile.getServerable() instanceof SGServerable serverable) || serverable.getGameState() != GameState.LOBBY) {
            profile.sendMessage("&8[&3Xime&8] &cYou cannot use this command right now.");
            return true;
        }
        plugin.getDisguiseHandler().disguise(profile);

        return true;
    }
}