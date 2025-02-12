package club.mcgamer.xime.hub.design.sidebar;

import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.SidebarType;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class HubSidebarAdapter extends SidebarAdapter {

    @Getter private final int updateRate = 10;

    @Override
    public String getTitle(Profile profile) {
//        if (profile.getPlayer().isSneaking()) {
//            return profile.getRank().getColor() + profile.getName() + " &8- &c5:00";
//        }

        return "&b&l" + profile.getLanguage().getServerIp();
    }

    @Override
    public List<String> getLines(Profile profile) {
        Serverable serverable = profile.getServerable();

        SidebarType sidebarType = SidebarType.values()[profile.getPlayerData().getSidebarType()];

        final String[] timeFormats = new String[3];
        ZonedDateTime dateTimeUser = ZonedDateTime.now(profile.getGeoLocationData() == null
                ? ZoneId.of("UTC") : profile.getGeoLocationData().getTimeZone());
        ZonedDateTime dateTimeLocal = ZonedDateTime.now(ZoneId.of("UTC"));

        String serverVersion = profile.getLanguage().getVersion();

        timeFormats[0] = dateTimeUser.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        timeFormats[1] = dateTimeUser.format(DateTimeFormatter.ofPattern("hh:mm a z"));
        timeFormats[2] = dateTimeLocal.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

        switch (sidebarType) {
            case MINIMIZE:
                return Arrays.asList(
                        String.format("&7EU%s %s", serverable.getServerId(), serverVersion),
                        ChatColor.GRAY + timeFormats[2],
                        "",
                        String.format("&6Online: &e%s", serverable.getPlayerList().size())
                );

            case TWENTY_FOURTEEN:
                return Arrays.asList(
                        "&6&l» Time",
                        ChatColor.WHITE + timeFormats[0],
                        ChatColor.WHITE + timeFormats[1],
                        "",
                        "&6&l» Server",
                        String.format("&3EU&8: &f%s", serverable.getServerId()),
                        "",
                        "&6&l» Players",
                        String.format("&fOnline: %s", serverable.getPlayerList().size())
                );
            case TWENTY_FIFTEEN:
                return Arrays.asList(
                        "&7&l» &fYou",
                        profile.getDisplayName(),
                        "",
                        "&7&l» &fTime",
                        ChatColor.YELLOW + timeFormats[0],
                        ChatColor.YELLOW + timeFormats[1],
                        ChatColor.GRAY + timeFormats[2],
                        "",
                        "&7&l»&f Server",
                        String.format("&6EU&8: &e%s", serverable.getServerId()),
                        "",
                        "&7&l»&f Players",
                        String.format("&6Online&8: &e%s", serverable.getPlayerList().size())
                );
        }
        //Default one
        return Arrays.asList(
                "&6&l» Time",
                ChatColor.WHITE + timeFormats[0],
                ChatColor.WHITE + timeFormats[1],
                ChatColor.GRAY + timeFormats[2],
                "",
                "&6&l» Server",
                String.format("&3EU&8: &f%s", serverable.getServerId()),
                "&7" + serverVersion,
                "",
                "&6&l» Players",
                String.format("&fOnline: %s", serverable.getPlayerList().size())
        );
    }

    @Override
    public void tick() {
    }
}
