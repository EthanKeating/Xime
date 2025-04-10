package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.util.TextUtil;
import com.j256.ormlite.field.DatabaseField;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResetStatsCommand extends XimeCommand {

    public ResetStatsCommand() {
        super("resetstats");
        this.description = "Reset stats command";
        this.usageMessage = "/resetstats [player]";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (args.length == 0) {
            profile.sendMessage("&cUsage: /resetstats [player/all]");
            return true;
        }

        String argument = args[0];

        if (argument.equalsIgnoreCase("all")) {

            List<PlayerData> allPlayers = plugin.getDataHandler().getPlayerData();

            for (PlayerData playerData : allPlayers) {
                updatePlayerData(playerData);
            }

            profile.sendMessage(String.format("&aSuccesfully reset &e%s &aplayers stats", allPlayers.size()));
            return true;
        }

        PlayerData playerData = plugin.getDataHandler().getPlayerData(argument);

        if (playerData == null)
            return true;

        updatePlayerData(playerData);
        profile.sendMessage(String.format("&aSuccesfully reset &e%s&a's stats", argument));

        return true;
    }

    private void updatePlayerData(PlayerData playerData) {
        playerData.setSgPoints(1000);
        playerData.setSgGamesWon(0);
        playerData.setSgGameRank(-1);
        playerData.setSgGamesPlayed(0);
        playerData.setSgDeathmatches(0);
        playerData.setSgKills(0);
        playerData.setSgMostKills(0);
        playerData.setSgDeaths(0);
        playerData.setSgChests(0);
        playerData.setSgMostChests(0);
        playerData.setSgLifeSpan(0);
        playerData.setSgLongestLifeSpan(0);
        playerData.setSgBountiesSent(0);
        playerData.setSgBountiesReceived(0);
        playerData.setBgSwordSlot(0);
        playerData.setBgRodSlot(1);
        playerData.setBgBowSlot(2);
        playerData.setBgFNSSlot(3);
        playerData.setBgGapSlot(4);
        playerData.setBgArrowSlot(8);
        playerData.setBgKills(0);
        playerData.setBgDeaths(0);
        playerData.setBgWins(0);
        playerData.setBgGameRank(-1);
        playerData.setBgBowKills(0);

        plugin.getDataHandler().updatePlayerData(playerData);
    }
}