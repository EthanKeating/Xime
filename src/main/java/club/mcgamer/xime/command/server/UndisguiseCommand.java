package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.staff.StaffServerable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class UndisguiseCommand extends XimeCommand {

    public UndisguiseCommand() {
        super("undisguise");
        this.description = "Get a random disguise";
        this.usageMessage = "/disguise";
        this.setAliases(Arrays.asList("ud"));
        setPermission("xime.diamond");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        String prefix = profile.getServerable().getPrefix();

        if (profile.getServerable() instanceof SGServerable serverable && serverable.getGameState() != GameState.LOBBY) {
            profile.sendMessage(prefix + "&4You cannot use this command right now&8.");
            return true;
        }

        if (profile.getServerable() instanceof StaffServerable ) {
            profile.sendMessage(prefix + "&4You cannot use this command right now&8.");
            return true;
        }

        if (profile.getServerable() instanceof BGServerable serverable && profile.getTemporaryData() instanceof BGTemporaryData temporaryData && !temporaryData.isWaiting()) {
            profile.sendMessage(prefix + "&4You cannot use this command right now&8.");
            return true;
        }

        plugin.getDisguiseHandler().undisguise(profile);

        return true;
    }
}