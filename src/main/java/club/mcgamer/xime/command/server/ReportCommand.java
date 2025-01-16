package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
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

    private final HashMap<Integer, String> reportIdMap = new HashMap<>();
    private final HashMap<Integer, ReportPriority> reportPriortyMap = new HashMap<>();

    public ReportCommand() {
        super("report");
        this.description = "report a player";
        this.usageMessage = "/report <player> <rule #> <message>";
        this.setAliases(Arrays.asList());

        register();

        reportIdMap.put(1, "Harassment");
        reportIdMap.put(2, "Inappropriate Language");
        reportIdMap.put(3, "Spam");
        reportIdMap.put(4, "Advertising");
        reportIdMap.put(5, "Revealing Personal Information");
        reportIdMap.put(6, "Hacking");
        reportIdMap.put(7, "Inappropriate Skin");
        reportIdMap.put(8, "Other");

        reportPriortyMap.put(1, ReportPriority.MEDIUM);
        reportPriortyMap.put(2, ReportPriority.LOW);
        reportPriortyMap.put(3, ReportPriority.LOW);
        reportPriortyMap.put(4, ReportPriority.MEDIUM);
        reportPriortyMap.put(5, ReportPriority.HIGH);
        reportPriortyMap.put(6, ReportPriority.CRITICAL);
        reportPriortyMap.put(7, ReportPriority.MEDIUM);
        reportPriortyMap.put(8, ReportPriority.MEDIUM);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

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
        String reportPriority = reportPriortyMap.get(reportId).getName();
        String reportDescription = args.length > 2 ?
                String.join(" ", Arrays.copyOfRange(args, 2, args.length))
                : "No description";

        if (argumentProfile.getDisguiseData() != null && !args[0].equalsIgnoreCase(argumentProfile.getDisguiseData().getName())) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat player is not online."));
            return true;
        }

        ZonedDateTime reportDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        Report report = new Report(argumentProfile.getDisplayNameBypassDisguise(),
                argumentProfile.getNameBypassDisguise(),
                argumentProfile.getUuid().toString(),
                reportReason,
                reportDescription,
                reportPriority,
                reportDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " @ " + reportDateTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                profile.getDisplayNameBypassDisguise(),
                profile.getNameBypassDisguise(),
                profile.getUuid().toString());

        plugin.getReportHandler().createReport(report);
        profile.sendMessage("&8[&3Xime&8] &bThank you for your report&8, &bthe player has been sent to our conviction system&8.");

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
