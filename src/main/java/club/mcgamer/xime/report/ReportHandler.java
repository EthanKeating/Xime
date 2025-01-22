package club.mcgamer.xime.report;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.report.impl.Report;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        executeWebhook(report, reporter);
    }

    @SneakyThrows
    public void executeWebhook(Report report, Profile reporter) {
        String json;
        StringEntity params;
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(plugin.getConfig().getString("webhook"));
        request.setHeader("Content-type", "application/json");
        json = new String(Files.readAllBytes(Paths.get(plugin.getDataFolder().getAbsolutePath(), "embeds", "report.json")));
        json = json.replace("PLAYER", report.getReportedName())
                .replace("EXECUTOR", reporter.getName())
                .replace("REASON", report.getReportedReason())
                .replace("DESCRIPTION", report.getReportDescription())
                .replace("PRIORITY", report.getReportPriority());
        params = new StringEntity(json);
        request.setEntity(params);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                httpClient.execute(request);
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to send notification. Is the webhook valid?");
            }
        });
    }

}
