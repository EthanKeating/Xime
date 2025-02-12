package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.report.impl.Report;
import club.mcgamer.xime.report.impl.ReportPriority;
import club.mcgamer.xime.staff.menu.ReportsMenu;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReportsCommand extends XimeCommand {
    public ReportsCommand() {
        super("reports");
        this.description = "view reports";
        this.usageMessage = "/reports";
        this.setAliases(Arrays.asList());
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!isPlayer(sender)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player)sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        new ReportsMenu(profile).open(player);

        return true;
    }

}
