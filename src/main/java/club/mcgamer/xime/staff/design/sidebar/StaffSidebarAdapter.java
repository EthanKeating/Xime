package club.mcgamer.xime.staff.design.sidebar;

import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.SidebarType;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import me.lucko.spark.api.statistic.StatisticWindow;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class
StaffSidebarAdapter extends SidebarAdapter {

    @Getter private final int updateRate = 10;

    private long memory;
    private long maxMemory;

    @Override
    public String getTitle(Profile profile) {
        return "    &b&lStaff Mode    ";
    }

    @Override
    public List<String> getLines(Profile profile) {

        final String[] timeFormats = new String[3];
        ZonedDateTime dateTimeUser = ZonedDateTime.now(profile.getGeoLocationData() == null
                ? ZoneId.of("UTC") : profile.getGeoLocationData().getTimeZone());
        ZonedDateTime dateTimeLocal = ZonedDateTime.now(ZoneId.of("UTC"));

        timeFormats[0] = dateTimeUser.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        timeFormats[1] = dateTimeUser.format(DateTimeFormatter.ofPattern("hh:mm a z"));
        timeFormats[2] = dateTimeLocal.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        DecimalFormat decimalFormat = new DecimalFormat("##0.0");

        Serverable serverable = profile.getServerable();


       return Arrays.asList(
               "&6&l» Time",
               ChatColor.WHITE + timeFormats[0],
               ChatColor.WHITE + timeFormats[1],
               ChatColor.GRAY + timeFormats[2],
               "",
               "&6&l» Network",
               String.format("&fTPS: &a%s", decimalFormat.format(serverable.getPlugin().getSpark().tps().poll(StatisticWindow.TicksPerSecond.SECONDS_5))),
               String.format("&fTick: &a%sms", decimalFormat.format(serverable.getPlugin().getSpark().mspt().poll(StatisticWindow.MillisPerTick.SECONDS_10).mean())),
               String.format("&fRam: &a%s MB", memory),
               String.format("&fOnline: &a%s", Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()),
               "&b&lMCGamer.club"
        );
    }

    @Override
    public void tick() {
        Runtime r = Runtime.getRuntime();
        this.memory = (r.totalMemory() - r.freeMemory()) / 1048576;
    }
}
