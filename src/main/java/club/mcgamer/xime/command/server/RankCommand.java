package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.soap.Text;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RankCommand extends XimeCommand {

    public RankCommand() {
        super("rank");
        this.description = "Set a player's rank";
        this.usageMessage = "/rank <player> <rank>";
        this.setAliases(Arrays.asList("setrank"));
        setPermission("xime.admin");

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
        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat player is not online."));
            return true;
        }

        if (rankHandler.getRank(args[1]) == null) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat rank is not valid."));
            sender.sendMessage(rankListString);
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        Profile profile = plugin.getProfileHandler().getProfile(player);
        Rank rank = rankHandler.getRank(args[1]);

        rankHandler.setRank(profile, rank);

        sender.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &bYou have set &e%s&b's rank to&8: &f%s%s&8.", player.getName(), rank.getColor(), rank.getName())));
        player.sendMessage(TextUtil.translate(String.format("&8[&3Xime&8] &bYour rank has been set to&8: &f%s%s&8.", rank.getColor(), rank.getName())));

        return true;
    }
}