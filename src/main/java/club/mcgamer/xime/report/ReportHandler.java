package club.mcgamer.xime.report;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.report.impl.Report;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;

@Getter
public class ReportHandler {

    private final XimePlugin plugin;
    private final ArrayList<Report> activeReports = new ArrayList<>();

    public ReportHandler(XimePlugin plugin) {
        this.plugin = plugin;
    }

    public void createReport(Report report, Profile reporter) {
        activeReports.add(report);

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("xime.staff")).forEach(player -> {
            player.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &b%s &bjust submitted a report&8. &bUse &8[&c/reports&8] &bto view it&8.", reporter.getDisplayNameBypassDisguise())));
        });


    }

}
