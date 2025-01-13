package club.mcgamer.xime.report;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.report.impl.Report;
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
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("xime.staff")).forEach(player -> {});
    }

}
