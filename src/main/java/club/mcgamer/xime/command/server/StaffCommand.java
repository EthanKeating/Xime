package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffCommand extends XimeCommand {

    public StaffCommand() {
        super("staff");
        this.description = "toggle staff mode";
        this.usageMessage = "/staff";
        this.setAliases(Arrays.asList("gamemaster", "mod", "staffmode", "modmode"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() instanceof StaffServerable) {
            plugin.getServerHandler().getFallback().add(profile);
            return true;
        }

        plugin.getServerHandler().getServerList()
                .stream()
                .filter(serverable -> serverable instanceof StaffServerable)
                .findAny()
                .ifPresent(serverable -> serverable.add(profile));

        return true;
    }
}
