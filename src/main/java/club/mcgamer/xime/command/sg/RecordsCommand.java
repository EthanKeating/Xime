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

public class RecordsCommand extends XimeCommand {

    public RecordsCommand() {
        super("records");
        this.description = "Check a player's records";
        this.usageMessage = "/records [player]";
        this.setAliases(Arrays.asList("debug", "db", "bug"));

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
                    profile.sendMessage(String.format(prefix + "%s&f's Records", argumentProfile.getDisplayName()));
                    profile.sendMessage(String.format(prefix + "&fRank&8: &e%s", argumentProfile.getRank()));
                    profile.sendMessage(String.format(prefix + "&fGames Won&8: &e%s", argumentProfile.getMockOrRealPlayerData().getBgWins()));
                    profile.sendMessage(String.format(prefix + "&fKills&8: &e%s", argumentProfile.getMockOrRealPlayerData().getBgKills()));
                    profile.sendMessage(String.format(prefix + "&fDeaths&8: &e%s", argumentProfile.getMockOrRealPlayerData().getBgDeaths()));
                    return true;
                }

                profile.sendMessage(String.format(prefix + "%s&f's Records", argumentProfile.getDisplayName()));
                profile.sendMessage(String.format(prefix + "&fRank&8: &e%s", argumentProfile.getRank()));
                profile.sendMessage(String.format(prefix + "&fPoints&8: &e%s", argumentProfile.getMockOrRealPlayerData().getSgPoints()));
                profile.sendMessage(String.format(prefix + "&fGame Rank&8: &8#&e%s", argumentProfile.getMockOrRealPlayerData().getSgGameRank() == -1 ? "-" : argumentPlayerData.getSgGameRank()));
                profile.sendMessage(String.format(prefix + "&fGames (Won/Total)&8: &e%s&8/&e%s", argumentProfile.getMockOrRealPlayerData().getSgGamesWon(), argumentPlayerData.getSgGamesPlayed()));
                profile.sendMessage(String.format(prefix + "&fKills (Round/Total)&8: &e%s&8/&e%s", argumentProfile.getMockOrRealPlayerData().getSgMostKills(), argumentPlayerData.getSgKills()));
                profile.sendMessage(String.format(prefix + "&fChests (Round/Total)&8: &e%s&8/&e%s", argumentProfile.getMockOrRealPlayerData().getSgMostChests(), argumentPlayerData.getSgChests()));
                profile.sendMessage(String.format(prefix + "&fLifespan (Round/Total)&8: &e%s&8/&e%s", convertDuration(argumentProfile.getMockOrRealPlayerData().getSgLongestLifeSpan()).replace('m', ':').replace("s", ""), convertDuration(argumentProfile.getMockOrRealPlayerData().getSgLifeSpan())));
                return true;
            }
        }

        if (profile.getServerable() instanceof BGServerable) {
            profile.sendMessage(String.format(prefix + "%s&f's Records", displayName));
            profile.sendMessage(String.format(prefix + "&fRank&8: &e%s", rankName));
            profile.sendMessage(String.format(prefix + "&fGames Won&8: &e%s", argumentPlayerData.getBgWins()));
            profile.sendMessage(String.format(prefix + "&fKills&8: &e%s", argumentPlayerData.getBgKills()));
            profile.sendMessage(String.format(prefix + "&fDeaths&8: &e%s", argumentPlayerData.getBgDeaths()));
            return true;
        }

        profile.sendMessage(String.format(prefix + "%s&f's Records", displayName));
        profile.sendMessage(String.format(prefix + "&fRank&8: &e%s", rankName));
        profile.sendMessage(String.format(prefix + "&fPoints&8: &e%s", argumentPlayerData.getSgPoints()));
        profile.sendMessage(String.format(prefix + "&fGame Rank&8: &8#&e%s", argumentPlayerData.getSgGameRank() == -1 ? "-" : argumentPlayerData.getSgGameRank()));
        profile.sendMessage(String.format(prefix + "&fGames (Won/Total)&8: &e%s&8/&e%s", argumentPlayerData.getSgGamesWon(), argumentPlayerData.getSgGamesPlayed()));
        profile.sendMessage(String.format(prefix + "&fKills (Round/Total)&8: &e%s&8/&e%s", argumentPlayerData.getSgMostKills(), argumentPlayerData.getSgKills()));
        profile.sendMessage(String.format(prefix + "&fChests (Round/Total)&8: &e%s&8/&e%s", argumentPlayerData.getSgMostChests(), argumentPlayerData.getSgChests()));
        profile.sendMessage(String.format(prefix + "&fLifespan (Round/Total)&8: &e%s&8/&e%s", convertDuration(argumentPlayerData.getSgLongestLifeSpan()).replace('m', ':').replace("s", ""), convertDuration(argumentPlayerData.getSgLifeSpan())));

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