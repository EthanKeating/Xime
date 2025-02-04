package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.RankHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class UnmaskCommand extends XimeCommand {

    public UnmaskCommand() {
        super("unmask");
        this.description = "Unmask your rank";
        this.usageMessage = "/unmask";
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

        if (profile.getDisguiseData() == null || profile.getRank() == profile.getRankBypassDisguise()) {
            profile.sendMessage(profile.getServerable().getPrefix() + "&cYou are not masked.");
            return true;
        }

        //player has disguise name, so set disguise data rank back to default rank
        if (!profile.getName().equalsIgnoreCase(profile.getNameBypassDisguise()))
            profile.getDisguiseData().setRank(RankHandler.DEFAULT_RANK);
        else
            profile.setDisguiseData(null);

        profile.sendMessage(profile.getServerable().getPrefix() + "&fYou have been unmasked");
        return true;
    }
}