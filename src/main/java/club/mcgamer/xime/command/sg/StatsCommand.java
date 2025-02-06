package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StatsCommand extends XimeCommand {

    public StatsCommand() {
        super("stats");
        this.description = "Check a player's stats";
        this.usageMessage = "/stats [player]";
        this.setAliases(Arrays.asList("statistics"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        ProfileHandler profileHandler = plugin.getProfileHandler();
        Profile profile = profileHandler.getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class, BGServerable.class)) return true;

        DataHandler dataHandler = plugin.getDataHandler();
        String prefix = profile.getServerable().getPrefix();

        PlayerData argumentPlayerData = args.length == 0
                ? profile.getPlayerData()
                : Bukkit.getPlayer(args[0]) != null
                    ? profileHandler.getProfile(Bukkit.getPlayer(args[0])).getPlayerData()
                    : dataHandler.getPlayerData(args[0]);

        if (argumentPlayerData == null) {
            profile.sendMessage(prefix + " &cThat player has never joined the server!");
            return true;
        }

        //Profile argumentProfile = profileHandler.getProfile(argumentPlayer.getUniqueId());

        //PlayerData playerData = argumentProfile != null ? argumentProfile.getMockOrRealPlayerData() : dataHandler.getPlayerData(argumentPlayer.getUniqueId());

        String rankName = argumentPlayerData.getUserRank();
        String displayName = argumentPlayerData.getDisplayName();

        if (args.length > 0 && Bukkit.getPlayer(args[0]) != null) {
            Profile argumentProfile = profileHandler.getProfile(Bukkit.getPlayer(args[0]));

            if (argumentProfile.getDisguiseData() != null && !argumentProfile.getName().equalsIgnoreCase(args[0])) {

            } else {

                if (profile.getServerable() instanceof BGServerable) {
                    profile.sendMessage(String.format("&8&m----&2 %s&f's stats &8&m----", argumentProfile.getDisplayName()));
                    profile.sendMessage(String.format("&fGames Won&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getBgWins()));
                    profile.sendMessage(String.format("&fKills&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getBgKills()));
                    profile.sendMessage(String.format("&fDeaths&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getBgDeaths()));
                    profile.sendMessage("&fKill / Death Ratio&8: &e" + String.format("%.1f", (double) argumentProfile.getMockOrRealPlayerData().getBgKills() / (double) Math.max(1, argumentProfile.getMockOrRealPlayerData().getBgDeaths())));
                    return true;
                }

                profile.sendMessage(String.format("&8&m----&2 %s&f's stats &8&m----", argumentProfile.getDisplayName()));
                profile.sendMessage(String.format("&fChests Opened&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgChests()));
                profile.sendMessage(String.format("&fGames Played&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgGamesPlayed()));
                profile.sendMessage(String.format("&fPlayer Kills&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgKills()));
                profile.sendMessage(String.format("&fTotal Lifespan&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgLifeSpan() / 1000));
                profile.sendMessage(String.format("&fGames Won&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgGamesWon()));
                profile.sendMessage(String.format("&fDeathmatches&8: &e%s.0", argumentProfile.getMockOrRealPlayerData().getSgDeathmatches()));
                profile.sendMessage("&fKill / Death Ratio&8: &e" + String.format("%.1f", (double) argumentProfile.getMockOrRealPlayerData().getSgKills() / (double) Math.max(1, argumentProfile.getMockOrRealPlayerData().getSgDeaths())));
                return true;
            }
        }

        if (profile.getServerable() instanceof BGServerable) {
            profile.sendMessage(String.format("&8&m----&2 %s&f's stats &8&m----", displayName));
            profile.sendMessage(String.format("&fGames Won&8: &e%s.0", argumentPlayerData.getBgWins()));
            profile.sendMessage(String.format("&fKills&8: &e%s.0", argumentPlayerData.getBgKills()));
            profile.sendMessage(String.format("&fDeaths&8: &e%s.0", argumentPlayerData.getBgDeaths()));
            profile.sendMessage("&fKill / Death Ratio&8: &e" + String.format("%.1f", (double) argumentPlayerData.getBgKills() / (double) Math.max(1, argumentPlayerData.getBgDeaths())));
            return true;
        }

        profile.sendMessage(String.format("&8&m----&2 %s&f's stats &8&m----", displayName));
        profile.sendMessage(String.format("&fChests Opened&8: &e%s.0", argumentPlayerData.getSgChests()));
        profile.sendMessage(String.format("&fGames Played&8: &e%s.0", argumentPlayerData.getSgGamesPlayed()));
        profile.sendMessage(String.format("&fPlayer Kills&8: &e%s.0", argumentPlayerData.getSgKills()));
        profile.sendMessage(String.format("&fTotal Lifespan&8: &e%s.0", argumentPlayerData.getSgLifeSpan() / 1000));
        profile.sendMessage(String.format("&fGames Won&8: &e%s.0", argumentPlayerData.getSgGamesWon()));
        profile.sendMessage(String.format("&fDeathmatches&8: &e%s.0", argumentPlayerData.getSgDeathmatches()));
        profile.sendMessage("&fKill / Death Ratio&8: &e" + String.format("%.1f", (double)argumentPlayerData.getSgKills() / (double)Math.max(1, argumentPlayerData.getSgDeaths())));

        return true;
    }

    private String convertDuration(long durationInMillis) {
        long totalSeconds = durationInMillis / 1000;

        long weeks = totalSeconds / (7 * 24 * 60 * 60);
        totalSeconds %= (7 * 24 * 60 * 60);

        long days = totalSeconds / (24 * 60 * 60);
        totalSeconds %= (24 * 60 * 60);

        long hours = totalSeconds / (60 * 60);
        totalSeconds %= (60 * 60);

        long minutes = totalSeconds / 60;
        totalSeconds %= 60;

        long seconds = totalSeconds;

        StringBuilder result = new StringBuilder();
        if (weeks > 0) result.append(weeks).append("w");
        if (days > 0) result.append(days).append("d");
        if (hours > 0) result.append(hours).append("h");
        if (minutes > 0) result.append(minutes).append("m");
        if (seconds >= 0) result.append(seconds).append("s");

        return result.toString().trim();
    }

}