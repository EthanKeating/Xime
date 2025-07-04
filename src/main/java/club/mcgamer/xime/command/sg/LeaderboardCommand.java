package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LeaderboardCommand extends XimeCommand {

    public LeaderboardCommand() {
        super("leaderboard");
        this.description = "Check a player's stats";
        this.usageMessage = "/leaderboard [page 1-10]";
        this.setAliases(Arrays.asList("lb", "top"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        ProfileHandler profileHandler = plugin.getProfileHandler();
        Profile profile = profileHandler.getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, BGServerable.class)) return true;

        Serverable serverable = profile.getServerable();
        String prefix = serverable.getPrefix();

        int page;

        if (args.length > 0) {
            if (args[0].matches("^10|[1-9]$")) {
                page = Integer.parseInt(args[0]);
            } else {
                page = 1;
                hasArgs(sender, args, 1000);
                return true;
            }
        } else {
            page = 1;
        }
        int topPlayers = 10 * page;

        profile.sendMessage(String.format(prefix + "&fTop %s Leaderboard", topPlayers));
        if (serverable instanceof BGServerable) {
            for (int i = 1 + (10 * (page - 1)); i <= 10 + (10 * (page - 1)); i++) {

                if (plugin.getDataHandler().getTopBGPlayerData().isEmpty()) break;
                if (i >= plugin.getDataHandler().getTopBGPlayerData().size()) break;

                PlayerData playerData = plugin.getDataHandler().getTopBGPlayerData().get(i - 1);

                if (playerData == null) break;

                profile.sendMessage(prefix + "&e#" + i + "&8) &f" + playerData.getDisplayName() + " &8- &f" + playerData.getBgKills() + " kills");
            }
        }
        if (serverable instanceof SGServerable) {
            for (int i = 1 + (10 * (page - 1)); i <= 10 + (10 * (page - 1)); i++) {

                if (plugin.getDataHandler().getTopPlayerData().isEmpty()) break;
                if (i >= plugin.getDataHandler().getTopPlayerData().size()) break;

                PlayerData playerData = plugin.getDataHandler().getTopPlayerData().get(i - 1);

                if (playerData == null) break;

                profile.sendMessage(prefix + "&e#" + i + "&8) &f" + playerData.getDisplayName() + " &8- &f" + playerData.getSgGamesWon() + " wins");
            }
        }

        return true;
    }

}