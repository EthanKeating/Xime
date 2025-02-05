package club.mcgamer.xime.command.sg;

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

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class)) return true;

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

        String displayName = argumentPlayerData.getDisplayName();
        String rankName = argumentPlayerData.getUserRank();
//
//        if (args.length > 0 && argumentProfile != null && argumentProfile.getDisguiseData() != null && argumentProfile.getNameBypassDisguise().equalsIgnoreCase(args[0])) {
//            displayName = argumentProfile.getDisplayNameBypassDisguise();
//            playerData = argumentProfile.getPlayerData();
//            rankName = argumentProfile.getRankBypassDisguise().getName();
//        }

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