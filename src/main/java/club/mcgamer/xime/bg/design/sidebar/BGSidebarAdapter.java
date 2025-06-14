package club.mcgamer.xime.bg.design.sidebar;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.bg.data.BGTemporaryData;
import club.mcgamer.xime.bg.leaderboard.LeaderboardEntry;
import club.mcgamer.xime.design.sidebar.SidebarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.SidebarType;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BGSidebarAdapter extends SidebarAdapter {

    @Getter private final int updateRate = 10;

    @Override
    public String getTitle(Profile profile) {
        if (profile.getServerable() instanceof BGServerable) {
            BGServerable serverable = (BGServerable) profile.getServerable();
            GameTimer gameTimer = serverable.getGameTimer();

            return String.format("&a%s &8- &c%s", profile.getRank().getColor() + profile.getNameBypassDisguise(), gameTimer.toString());
        }

        return "";
    }

    @Override
    public List<String> getLines(Profile profile) {
        if (profile.getServerable() instanceof BGServerable) {

            final String[] timeFormats = new String[3];

            ZonedDateTime dateTimeUser = ZonedDateTime.now(profile.getGeoLocationData() == null
                    ? ZoneId.of("UTC") : profile.getGeoLocationData().getTimeZone());
            ZonedDateTime dateTimeLocal = ZonedDateTime.now(ZoneId.of("UTC"));

            timeFormats[0] = dateTimeUser.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            timeFormats[1] = dateTimeUser.format(DateTimeFormatter.ofPattern("hh:mm a z"));
            timeFormats[2] = dateTimeLocal.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

            BGServerable serverable = (BGServerable) profile.getServerable();
            BGTemporaryData temporaryData = (BGTemporaryData) profile.getTemporaryData();
            SidebarType sidebarType = SidebarType.values()[profile.getPlayerData().getSidebarType()];
            List<String> lines = new ArrayList<>();

            switch (sidebarType) {
                case DEFAULT:
                    lines = new ArrayList<>(Arrays.asList(
                            "&6&l» Time",
                            ChatColor.WHITE + timeFormats[0],
                            ChatColor.WHITE + timeFormats[1],
                            ChatColor.GRAY + timeFormats[2],
                            "",
                            "&6&l» Server",
                            String.format("&3EU&8: &f%s", serverable.getServerId()),
                            ""
                    ));
                    for(int i = 0; i < 5; i++) {
                        if (serverable.getSortedLeaderboard().size() <= i) break;

                        LeaderboardEntry topEntry = serverable.getSortedLeaderboard().get(i);
                        lines.add(topEntry.getDisplayName() + "&f: " + topEntry.getKills());
                    }
                    lines.add("&3You&f: " + temporaryData.getKills());
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
                    break;
                case MINIMIZE:
                    lines = new ArrayList<>(Arrays.asList(
                            String.format("&7EU%s %s", serverable.getServerId(), profile.getLanguage().getVersion()),
                            ChatColor.GRAY + timeFormats[2],
                            ""
                    ));

                    for(int i = 0; i < 5; i++) {
                        if (serverable.getSortedLeaderboard().size() <= i) break;

                        LeaderboardEntry topEntry = serverable.getSortedLeaderboard().get(i);
                        lines.add(topEntry.getDisplayName() + "&f: " + topEntry.getKills());
                    }

                    lines.add("&3You&f: " + temporaryData.getKills());
                    lines.add("");
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
                    break;
                case TWENTY_FIFTEEN:
                    lines = new ArrayList<>(Arrays.asList(
                            "&7&l» &fTime",
                            ChatColor.YELLOW + timeFormats[0],
                            ChatColor.YELLOW + timeFormats[1],
                            ChatColor.GRAY + timeFormats[2],
                            "",
                            "&7&l»&f Server",
                            String.format("&6EU&8: &e%s", serverable.getServerId()),
                            ""
                    ));

                    for(int i = 0; i < 5; i++) {
                        if (serverable.getSortedLeaderboard().size() <= i) break;

                        LeaderboardEntry topEntry = serverable.getSortedLeaderboard().get(i);
                        lines.add(topEntry.getDisplayName() + "&f: " + topEntry.getKills());
                    }

                    lines.add("&3You&f: " + temporaryData.getKills());
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
                    break;
                case TWENTY_FOURTEEN:
                    lines = new ArrayList<>(Arrays.asList(
                            "&6&l» Time",
                            ChatColor.WHITE + timeFormats[0],
                            ChatColor.WHITE + timeFormats[1],
                            "",
                            "&6&l» Server     &7",
                            String.format("&3EU&8: &f%s", serverable.getServerId()),
                            "",
                            "&6&l» Game"
                    ));
                    for(int i = 0; i < 5; i++) {
                        if (serverable.getSortedLeaderboard().size() <= i) break;

                        LeaderboardEntry topEntry = serverable.getSortedLeaderboard().get(i);
                        lines.add(ChatColor.stripColor(topEntry.getName()) + ": " + topEntry.getKills());
                    }
                    break;
            }
            return lines;
        }
        return Collections.emptyList();
    }

    @Override
    public void tick() {
    }
}
