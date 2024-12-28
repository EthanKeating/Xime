package club.mcgamer.xime.hub.design.sidebar;

import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class HubSidebarAdapter extends SidebarAdapter {

    @Getter private final int updateRate = 10;

    @Override
    public String getTitle(Profile profile) {
        return "&b&lMCGamer.club";
    }

    @Override
    public List<String> getLines(Profile profile) {
        Serverable serverable = profile.getServerable();

        final String[] timeFormats = new String[3];
        ZonedDateTime dateTimeUser = ZonedDateTime.now(profile.getGeoLocationData() == null
                ? ZoneId.of("UTC") : profile.getGeoLocationData().getTimeZone());
        ZonedDateTime dateTimeLocal = ZonedDateTime.now(ZoneId.of("UTC"));

        timeFormats[0] = dateTimeUser.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        timeFormats[1] = dateTimeUser.format(DateTimeFormatter.ofPattern("hh:mm a z"));
        timeFormats[2] = dateTimeLocal.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        return Arrays.asList(
                "&6&l» Time",
                ChatColor.WHITE + timeFormats[0],
                ChatColor.WHITE + timeFormats[1],
                ChatColor.GRAY + timeFormats[2],
                "",
                "&6&l» Server",
                String.format("&3EU&8: &f%s", serverable.getServerId()),
                "&75.0.0",
                "",
                "&6&l» Players",
                String.format("&fOnline: %s", serverable.getPlayerList().size())
        );
    }

    @Override
    public void tick() {
    }
}
