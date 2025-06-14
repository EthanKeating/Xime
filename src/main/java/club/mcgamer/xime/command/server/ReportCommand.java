package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.report.impl.Report;
import club.mcgamer.xime.report.impl.ReportPriority;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReportCommand extends XimeCommand {

    private final Map<Integer, String> reportIdMap = Map.of(
            1, "Harassment",
            2, "Inappropriate Language",
            3, "Spam",
            4, "Advertising",
            5, "Revealing Personal Information",
            6, "Hacking",
            7, "Inappropriate Skin",
            8, "Other"
    );
    private final Map<Integer, ReportPriority> reportPriorityMap = Map.of(
            1, ReportPriority.MEDIUM,
            2, ReportPriority.LOW,
            3, ReportPriority.LOW,
            4, ReportPriority.MEDIUM,
            5, ReportPriority.HIGH,
            6, ReportPriority.CRITICAL,
            7, ReportPriority.MEDIUM,
            8, ReportPriority.MEDIUM
    );

    public ReportCommand() {
        super("report");
        this.description = "report a player";
        this.usageMessage = "/report <player> <rule #> <message>";
        this.setAliases(Arrays.asList());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        CooldownData cooldownData = profile.getCooldownData();
        if (cooldownData.hasReportCooldown(10)) return true;

        if (args.length < 2) {
            sendHelpMessage(profile);
            return true;
        }

        Player argumentPlayer = isPlayer(player, args[0]);
        if (argumentPlayer == null) return true;
        if (argumentPlayer == player) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cYou cannot report yourself!"));
            return true;
        }

        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);
        if (!args[1].matches(String.format("^[1-%s]", reportIdMap.size()))) {
            sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &cThere is no rule with the number &e%s&8.", args[1])));
            return true;
        }

        int reportId = Integer.parseInt(args[1]);
        String reportReason = reportIdMap.get(reportId);
        String reportPriority = reportPriorityMap.get(reportId).getName();
        String reportDescription = args.length > 2 ?
                String.join(" ", Arrays.copyOfRange(args, 2, args.length))
                : "No description";

        if (argumentProfile.getDisguiseData() != null && !args[0].equalsIgnoreCase(argumentProfile.getDisguiseData().getName())) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat player is not online."));
            return true;
        }

        ZonedDateTime reportDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Report report = new Report(argumentProfile,
                argumentProfile.getDisplayNameBypassDisguise(),
                argumentProfile.getNameBypassDisguise(),
                argumentProfile.getUuid().toString(),
                reportReason,
                reportDescription,
                reportDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " @ " + reportDateTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                reportPriority,
                profile.getDisplayNameBypassDisguise(),
                profile.getNameBypassDisguise(),
                profile.getUuid().toString());

        plugin.getReportHandler().createReport(report, profile);
        profile.sendMessage("&8[&3Xime&8] &bThank you for your report&8, &bthe player has been sent to our conviction system&8.");
        cooldownData.setReportCooldown();

        return true;
    }

    private void sendHelpMessage(Profile profile) {
        profile.sendMessage("&8[&3Xime&8] &8&m-------------------&3 Reporting &8&m-------------------")
                .sendMessage("&8[&3Xime&8] &6/report &7<player> <Rule #> <description>")
                .sendMessage("&8[&3Xime&8] &3Offences");
        for(Map.Entry<Integer, String> reportEntry : reportIdMap.entrySet()) {
            profile.sendMessage(String.format("&8[&3Xime&8] &8%s &7%s", reportEntry.getKey(), reportEntry.getValue()));
        }
    }
}
