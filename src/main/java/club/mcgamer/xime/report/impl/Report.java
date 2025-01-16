package club.mcgamer.xime.report.impl;

import club.mcgamer.xime.fastinv.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.format.DateTimeFormatter;

@Getter
public class Report {

    private final String reportedDisplayName;
    private final String reportedName;
    private final String reportedUUID;
    private final String reportedReason;
    private final String reportDescription;
    private final String reportedDateTime;

    private final String reportPriority;
    private final String reporterDisplayName;
    private final String reporterName;
    private final String reporterUUID;

    private final ItemStack itemStack;


    public Report(String reportedDisplayName, String reportedName, String reportedUUID, String reportedReason, String reportDescription, String reportedDateTime, String reportPriority, String reporterDisplayName, String reporterName, String reporterUUID) {
        this.reportedDisplayName = reportedDisplayName;
        this.reportedName = reportedName;
        this.reportedUUID = reportedUUID;
        this.reportedReason = reportedReason;
        this.reportDescription = reportDescription;
        this.reportedDateTime = reportedDateTime;
        this.reportPriority = reportPriority;
        this.reporterDisplayName = reporterDisplayName;
        this.reporterName = reporterName;
        this.reporterUUID = reporterUUID;

        itemStack = new ItemBuilder(Material.SKULL_ITEM)
                .data(3)
                .owner(Bukkit.getPlayer(reportedUUID))
                .name(reportedDisplayName)
                .lore(
                        "&7[" + reportedDateTime + "]",
                        "&8----------------------",
                        "&eReasons",
                        " &8* &ef" + reportedReason,
                        " &8* &e"  + reportDescription,
                        "&8----------------------",
                        " &bLeft Click &7to &b&lTeleport",
                        "&bRight Click&7 for more info",
                        "&bShift Click &7to &b&lClear",
                        "&8----------------------"
                        )
                .build();
    }
}
