package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class RankCommand extends XimeCommand {

    public RankCommand() {
        super("rank");
        this.description = "Set a player's rank";
        this.usageMessage = "/rank <player> <rank>";
        this.setAliases(Arrays.asList("setrank"));
        setPermission("xime.owner");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!hasPermission(sender)) return true;
        RankHandler rankHandler = plugin.getRankHandler();
        String rankListString = TextUtil.translate("&8[&3Xime&8] &fRanks: "
                + rankHandler.getRankList()
                .stream()
                .map(rank -> TextUtil.translate(rank.getColor() + rank.getName()))
                .collect(Collectors.joining("&8, &f")));

        if (!hasArgs(sender, args, 2)) {
            sender.sendMessage(rankListString);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            Rank rank = rankHandler.getRank(args[1]);

            if (rank == null) {
                sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat rank is not valid."));
                sender.sendMessage(rankListString);
                return;
            }
            if (sender instanceof Player) {
                Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
                if(plugin.getRankHandler().getRankList().indexOf(rank) < plugin.getRankHandler().getRankList().indexOf(profile.getRankBypassDisguise())) {
                    sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cYou do not have permission to set that rank."));
                    return;
                }
            }

            PlayerData playerData = plugin.getDataHandler().getPlayerData(args[0]);
            Profile profile = plugin.getProfileHandler().getProfile(UUID.fromString(playerData.getUuid()));

            if (profile == null) {
                DataHandler dataHandler = plugin.getDataHandler();

                playerData.setUserRank(rank.getName());
                playerData.setDisplayName(rank.getColor() + args[0]);

                dataHandler.updatePlayerData(playerData);
            } else {
                profile.setRank(rank);
                profile.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &bYour rank has been set to&8: &f%s%s&8.", rank.getColor(), rank.getName())));
            }

            sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &bYou have set &e%s&b's rank to&8: &f%s%s&8.", args[0], rank.getColor(), rank.getName())));
        });
        return true;
    }
}