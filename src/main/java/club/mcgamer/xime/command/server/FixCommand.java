package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FixCommand extends XimeCommand {

    public FixCommand() {
        super("fix");
        this.description = "Fix your position";
        this.usageMessage = "/fix";
        this.setAliases(Arrays.asList("gf", "ghostfix", "ghost"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        Location toLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        Location backLocation = player.getLocation();

        player.teleport(toLocation);
        player.teleport(backLocation);
        profile.sendMessage("&8[&3Xime&8] &fYou have been de-ghosted&8.");

        return true;
    }

}
