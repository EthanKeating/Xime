package club.mcgamer.xime.sg.design.sidebar;

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

public class SGSidebarAdapter extends SidebarAdapter {

    @Getter private final int updateRate = 10;

    @Override
    public String getTitle(Profile profile) {
        if (profile.getServerable() instanceof SGServerable) {
            SGServerable serverable = (SGServerable) profile.getServerable();
            GameTimer gameTimer = serverable.getGameTimer();
            GameState gameState = serverable.getGameState();

            return String.format("&a%s &c%s", gameState.getName(), gameTimer.toString());
        }

        return "";
    }

    @Override
    public List<String> getLines(Profile profile) {
        if (profile.getServerable() instanceof SGServerable) {

            final String[] timeFormats = new String[3];

            ZonedDateTime dateTimeUser = ZonedDateTime.now(profile.getGeoLocationData() == null
                    ? ZoneId.of("UTC") : profile.getGeoLocationData().getTimeZone());
            ZonedDateTime dateTimeLocal = ZonedDateTime.now(ZoneId.of("UTC"));

            timeFormats[0] = dateTimeUser.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            timeFormats[1] = dateTimeUser.format(DateTimeFormatter.ofPattern("hh:mm a z"));
            timeFormats[2] = dateTimeLocal.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

            SGServerable serverable = (SGServerable) profile.getServerable();
            GameState gameState = serverable.getGameState();
            SidebarType sidebarType = SidebarType.values()[profile.getPlayerData().getSidebarType()];
            List<String> lines = new ArrayList<>();

            switch (sidebarType) {
                case MINIMIZE:
                    lines = new ArrayList<>(Arrays.asList(
                            String.format("&7EU%s %s", serverable.getServerId(), profile.getLanguage().getVersion()),
                            ChatColor.GRAY + timeFormats[2],
                            ""
                    ));
                    if (gameState == GameState.LOBBY) {
                        lines.add(String.format("&fPlaying: %s", serverable.getPlayerList().size()));
                    } else {
                        lines.add(String.format("&fPlaying: %s", serverable.getTributeList().size()));
                        lines.add(String.format("&fWatching: %s", serverable.getSpectatorList().size()));
                    }
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
                    break;
                case TWENTY_FOURTEEN:
                    lines = new ArrayList<>(Arrays.asList(
                            "&6&l» Time",
                            ChatColor.WHITE + timeFormats[0],
                            ChatColor.WHITE + timeFormats[1],
                            "",
                            "&6&l» Server",
                            String.format("&3EU&8: &f%s", serverable.getServerId()),
                            "",
                            "&6&l» Players"
                    ));
                    if (gameState == GameState.LOBBY) {
                        lines.add(String.format("&fPlaying: %s", serverable.getPlayerList().size()));
                    } else {
                        lines.add(String.format("&fPlaying: %s", serverable.getTributeList().size()));
                        lines.add(String.format("&fWatching: %s", serverable.getSpectatorList().size()));
                    }
                    break;
                case TWENTY_FIFTEEN:
                    lines = new ArrayList<>(Arrays.asList(
                            "&7&l» &fYou",
                            profile.getDisplayNameBypassDisguise(),
                            "",
                            "&7&l» &fTime",
                            ChatColor.YELLOW + timeFormats[0],
                            ChatColor.YELLOW + timeFormats[1],
                            ChatColor.GRAY + timeFormats[2],
                            "",
                            "&7&l»&f Server",
                            String.format("&6EU&8: &e%s", serverable.getServerId()),
                            "",
                            "&7&l»&f Players"
                    ));
                    if (gameState == GameState.LOBBY) {
                        lines.add(String.format("&fPlaying: %s", serverable.getPlayerList().size()));
                    } else {
                        lines.add(String.format("&fPlaying: %s", serverable.getTributeList().size()));
                        lines.add(String.format("&fWatching: %s", serverable.getSpectatorList().size()));
                    }
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
                    break;
                default:
                    lines = new ArrayList<>(Arrays.asList(
                            "&6&l» Time",
                            ChatColor.WHITE + timeFormats[0],
                            ChatColor.WHITE + timeFormats[1],
                            ChatColor.GRAY + timeFormats[2],
                            "",
                            "&6&l» Server",
                            String.format("&3EU&8: &f%s", serverable.getServerId()),
                            "&7" + profile.getLanguage().getVersion(),
                            "",
                            "&6&l» Players"
                    ));
                    if (gameState == GameState.LOBBY) {
                        lines.add(String.format("&fPlaying: %s", serverable.getPlayerList().size()));
                    } else {
                        lines.add(String.format("&fPlaying: %s", serverable.getTributeList().size()));
                        lines.add(String.format("&fWatching: %s", serverable.getSpectatorList().size()));
                    }
                    lines.add("&b&l" + profile.getLanguage().getServerIp());
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
