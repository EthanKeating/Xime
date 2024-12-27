package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class UndisguiseCommand extends XimeCommand {

    public UndisguiseCommand() {
        super("undisguise");
        this.description = "Get a random disguise";
        this.usageMessage = "/disguise";
        this.setAliases(Arrays.asList("ud"));
        setPermission("xime.donor");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        Player player = profile.getPlayer();

        plugin.getDisguiseHandler().undisguise(profile);

        return true;
    }
}