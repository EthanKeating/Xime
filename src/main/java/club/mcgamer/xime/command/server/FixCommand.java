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
        this.setAliases(Arrays.asList("gf", "ghostfix", "ghost", "deghost"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        Location toLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        Location backLocation = player.getLocation().add(0.0, 0.3, 0.0);

        player.teleport(toLocation);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(backLocation);
            profile.sendMessage(profile.getServerable().getPrefix() + "&fYou have been de-ghosted&8.");
        }, 3L);

        return true;
    }

}
