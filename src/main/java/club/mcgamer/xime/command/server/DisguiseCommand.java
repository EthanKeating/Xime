package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class DisguiseCommand extends XimeCommand {

    public DisguiseCommand() {
        super("disguise");
        this.description = "Get a random disguise";
        this.usageMessage = "/disguise";
        this.setAliases(Arrays.asList("d"));
        setPermission("mcgamer.donor");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        Player player = profile.getPlayer();

        plugin.getDisguiseHandler().disguise(profile);

        return true;
    }
}