package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

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

        Bukkit.getOnlinePlayers().forEach(loopPlayer -> {
            loopPlayer.hidePlayer(player);
            player.hidePlayer(loopPlayer);
        });

        if (profile.getServerable() instanceof StaffServerable) {
            plugin.getServerHandler().getFallback().add(profile);
            profile.sendMessage("&8[&3Xime&8] &cYou have been removed from staff mode&8.");
            return true;
        }

        plugin.getServerHandler().getServerList()
                .stream()
                .filter(serverable -> serverable instanceof StaffServerable)
                .findAny()
                .ifPresent(serverable -> serverable.add(profile));

        profile.sendMessage("&8[&3Xime&8] &aYou have been added to staff mode&8.");

        PlayerUtil.refresh(profile);
        player.teleport(player.getLocation().add(0, 0.01, 0));
        player.setAllowFlight(true);
        player.setFlying(true);
        PlayerUtil.setFlying(profile);

        return true;
    }
}
