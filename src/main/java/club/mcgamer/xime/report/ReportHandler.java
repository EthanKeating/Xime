package club.mcgamer.xime.report;

import club.mcgamer.xime.XimePlugin;
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

    public void createReport(Report report) {
        activeReports.add(report);

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("xime.staff")).forEach(player -> {
            player.sendMessage(TextUtil.translate("&8[&3Xime&8] &bThank you for your report&8, &bthe player has been sent to our conviction system&8."));
        });


    }

}
