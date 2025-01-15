package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.DataHandler;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MaskCommand extends XimeCommand {

    public MaskCommand() {
        super("mask");
        this.description = "Mask as a different rank";
        this.usageMessage = "/mask <rank>";
        this.setAliases(Arrays.asList());
        setPermission("xime.iron");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player.getUniqueId());

        RankHandler rankHandler = plugin.getRankHandler();
        Rank originalRank = profile.getRankBypassDisguise();

        String rankListString = TextUtil.translate("&8[&3Xime&8] &fYour Masks: "
                + rankHandler.getRankList()
                .stream()
                .filter(otherRank -> plugin.getRankHandler().getRankList().indexOf(otherRank) > plugin.getRankHandler().getRankList().indexOf(originalRank))
                .map(rank -> TextUtil.translate(rank.getColor() + rank.getName()))
                .collect(Collectors.joining("&8, &f")));

        if (!hasArgs(sender, args, 1)) {
            profile.sendMessage(rankListString);
            return true;
        }

        Rank rank = rankHandler.getRank(args[0]);
        if (rank == null && !hasArgs(sender, args, 100)) return true;

        if (profile.getDisguiseData() != null)
            profile.getDisguiseData().setRank(rank);
        else
            profile.setDisguiseData(new DisguiseData(profile, profile.getUuid(), profile.getName(), profile.getSkin(), rank));

        profile.sendMessage(String.format("&8[&3Xime&8] &fYou have been masked as %s&8.", rank.getColor() + rank.getName()));

        return true;
    }
}