package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.SidebarType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class SidebarCommand extends XimeCommand {

    public SidebarCommand() {
        super("sidebar");
        this.description = "Change your sidebar style";
        this.usageMessage = "/sidebar [mode]";
        this.setAliases(Arrays.asList("sb"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        PlayerData playerData = profile.getPlayerData();
        String usageMessage = "&8[&3Xime&8] &fYou have to use: &2/sidebar &a[default, minimize, 2014, 2015]";

        if(args.length == 0) {
            profile.sendMessage(usageMessage);
            return true;
        }

        switch (args[0].toLowerCase()) {
                case "default":
                    playerData.setSidebarType(SidebarType.DEFAULT.ordinal());
                    break;
                case "minimize":
                    playerData.setSidebarType(SidebarType.MINIMIZE.ordinal());
                    break;
                case "2014":
                    playerData.setSidebarType(SidebarType.TWENTY_FOURTEEN.ordinal());
                    break;
                case "2015":
                    playerData.setSidebarType(SidebarType.TWENTY_FIFTEEN.ordinal());
                    break;
                default:
                    profile.sendMessage(usageMessage);
                    return true;
            }
        profile.sendMessage(String.format("&8[&3Xime&8] &fThe sidebar theme is changed to %s.", args[0].toLowerCase()));
        return true;
    }
}
