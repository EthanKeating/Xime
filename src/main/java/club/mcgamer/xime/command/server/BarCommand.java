package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.SidebarType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BarCommand extends XimeCommand {

    public BarCommand() {
        super("bossbar");
        this.description = "Toggle he bossbar";
        this.usageMessage = "/bossbar [mode]";
        this.setAliases(Arrays.asList("bar"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        PlayerData playerData = profile.getPlayerData();
        String prefix = profile.getServerable().getPrefix();


        return true;
    }
}
