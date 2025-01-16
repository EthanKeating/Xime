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
        this.setAliases(Arrays.asList("records"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        ProfileHandler profileHandler = plugin.getProfileHandler();
        Profile profile = profileHandler.getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class)) return true;

        OfflinePlayer argumentPlayer = args.length > 0 ? Bukkit.getOfflinePlayer(args[0]) : (OfflinePlayer) sender;
        String argumentPlayerName = args.length > 0 ? args[0] : ((OfflinePlayer) sender).getName();

        if (argumentPlayer == null) return true;

        DataHandler dataHandler = plugin.getDataHandler();

        if (!dataHandler.playerDataExists(argumentPlayer.getUniqueId())) {
            profile.sendMessage("&8[&6MCSG&8] &cThat player has never joined the server!");
            return true;
        }

        Profile argumentProfile = profileHandler.getProfile(argumentPlayer.getUniqueId());

        PlayerData playerData = argumentProfile != null ? argumentProfile.getMockOrRealPlayerData() : dataHandler.getPlayerData(argumentPlayer.getUniqueId());

        String displayName = argumentProfile != null ? argumentProfile.getDisplayName() : plugin.getRankHandler().getRank(playerData.getRank()).getColor() + argumentPlayerName;
        String rankName = argumentProfile != null ? argumentProfile.getRank().getName() : playerData.getRank();

        if (args.length > 0 && argumentProfile != null && argumentProfile.getDisguiseData() != null && argumentProfile.getNameBypassDisguise().equalsIgnoreCase(args[0])) {
            displayName = argumentProfile.getDisplayNameBypassDisguise();
            playerData = argumentProfile.getPlayerData();
            rankName = argumentProfile.getRankBypassDisguise().getName();
        }

        profile.sendMessage(String.format("&8[&6MCSG&8] %s&f's Records", displayName));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fRank&8: &e%s", rankName));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fPoints&8: &e%s", playerData.getSgPoints()));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fGame Rank&8: &8#&e%s", playerData.getSgGameRank() == -1 ? "-" : playerData.getSgGameRank()));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fGames (Won/Total)&8: &e%s&8/&e%s", playerData.getSgGamesWon(), playerData.getSgGamesPlayed()));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fKills (Round/Total)&8: &e%s&8/&e%s", playerData.getSgMostKills(), playerData.getSgKills()));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fChests (Round/Total)&8: &e%s&8/&e%s", playerData.getSgMostChests(), playerData.getSgChests()));
        profile.sendMessage(String.format("&8[&6MCSG&8] &fLifespan (Round/Total)&8: &e%s&8/&e%s", convertDuration(playerData.getSgLongestLifeSpan()).replace('m', ':').replace("s", ""), convertDuration(playerData.getSgLifeSpan())));

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