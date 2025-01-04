package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.impl.SidebarType;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SidebarCommand extends XimeCommand {

    public SidebarCommand() {
        super("sidebar");
        this.description = "Change your sidebar style";
        this.usageMessage = "/sidebar [mode]";
        this.setAliases(new ArrayList<String>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("default")) {
                profile.setSidebarType(SidebarType.DEFAULT);
                profile.sendMessage(String.format("&8[&3Xime&8] &fThe sidebar theme is now %s.", profile.getSidebarType().getName().toLowerCase()));
                return true;
            }
            if (args[0].equalsIgnoreCase("minimize") || args[0].equalsIgnoreCase("minimized")) {
                profile.setSidebarType(SidebarType.MINIMIZE);
                profile.sendMessage(String.format("&8[&3Xime&8] &fThe sidebar theme is now %s.", profile.getSidebarType().getName().toLowerCase()));
                return true;
            }
            if (args[0].equalsIgnoreCase("2014")) {
                profile.setSidebarType(SidebarType.TWENTY_FOURTEEN);
                profile.sendMessage(String.format("&8[&3Xime&8] &fThe sidebar theme is now %s.", profile.getSidebarType().getName().toLowerCase()));
                return true;
            }
            if (args[0].equalsIgnoreCase("2015")) {
                profile.setSidebarType(SidebarType.TWENTY_FIFTEEN);
                profile.sendMessage(String.format("&8[&3Xime&8] &fThe sidebar theme is now %s.", profile.getSidebarType().getName().toLowerCase()));
                return true;
            }
        }
        profile.sendMessage("&8[&3Xime&8] &fYou have to use: &2/sidebar &a[default, minimize, 2014, 2015]");

        return true;
    }
}
