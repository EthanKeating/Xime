package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.staff.StaffServerable;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GoToCommand extends XimeCommand {

    public GoToCommand() {
        super("goto");
        this.description = "Teleport to a player";
        this.usageMessage = "/goto [player]";
        this.setAliases(new ArrayList<>());
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        Player argumentPlayer = isPlayer(sender, args[0]);

        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (argumentProfile.getServerable() != profile.getServerable() && !(profile.getServerable() instanceof StaffServerable))
            argumentProfile.getServerable().add(profile);

        player.teleport(argumentPlayer.getLocation().add(0, 0.01, 0));
        profile.sendMessage(String.format("&8[&3Xime&8] &bYou have been teleported to &2%s&8.", argumentProfile.getDisplayName()));

        return true;
    }
}
