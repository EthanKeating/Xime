package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MakerCommand extends XimeCommand {

    public MakerCommand() {
        super("maker");
        this.description = "maker command";
        this.usageMessage = "/maker";
        this.setAliases(Arrays.asList("sgmaker"));
        setPermission("xime.platinum");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), HubServerable.class)) return true;

        SGMakerServerable serverable = new SGMakerServerable(profile);
        profile.sendMessage("&8[&3Xime&8] &fYour request for a custom server has been received.");
        serverable.add(profile);

        return true;
    }

}
