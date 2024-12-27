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

    private final String[] timeFormats = new String[3];

    @Getter private final int updateRate = 10;

    @Override
    public String getTitle(Profile profile) {
        return "&b&lMCGamer.club";
    }

    @Override
    public List<String> getLines(Profile profile) {
        Serverable serverable = profile.getServerable();

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
        //create time string[];
        ZonedDateTime dateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

        timeFormats[0] = dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        timeFormats[1] = dateTime.format(DateTimeFormatter.ofPattern("hh:mm a z"));
        timeFormats[2] = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
//
//        timeFormats[0] = "22 Dec 2024";
//        timeFormats[1] = "07:08 PM EST";
//        timeFormats[2] = "22/12/24 19:08";
    }
}
