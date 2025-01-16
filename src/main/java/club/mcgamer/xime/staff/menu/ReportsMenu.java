package club.mcgamer.xime.staff.menu;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.Material;

import java.util.concurrent.CopyOnWriteArrayList;

public class ReportsMenu extends FastInv {

    public ReportsMenu(Profile profile) {
        super(54, "MCGamer Reports");

        int index = 0;
        new CopyOnWriteArrayList<>(plugin.getReportHandler().getActiveReports()).forEach(report -> {
            setItem(index, report.getItemStack(), e -> {
                e.setCancelled(true);
                switch (e.getClick()) {
                    case SHIFT_LEFT:
                        case SHIFT_RIGHT:
                            plugin.getReportHandler().getActiveReports().remove(report);
                            profile.sendMessage(String.format("&8[&3Xime&8] %s&8'&bs report was cleared&8.", report.getReportedDisplayName()));
                            break;
                    case LEFT:
                        profile.getPlayer().performCommand("goto " + report.getReportedName());
                        break;
                    case RIGHT:
                        profile.sendMessage(String.format("&8[&3Xime&8] &fThere is &8[&6%s&8] &freport active&8.", plugin.getReportHandler().getActiveReports().size()))
                                .sendMessage(String.format("&7[%s] &8- &fReporter&8: &2%s &8- &fReason&8: &c%s", report.getReportedDateTime(), report.getReporterDisplayName(), report.getReportedReason()));
                        break;
                }
            });
        });
    }


}
