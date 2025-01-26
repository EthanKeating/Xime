package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.map.impl.MapPool;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class XimeInfoCommand extends XimeCommand {

    public XimeInfoCommand() {
        super("xime");
        this.description = "Xime info command";
        this.usageMessage = "/xime";
        this.setAliases(new ArrayList<String>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        profile.sendMessage("&8[&3Xime&8] &3&lXime &c&lV5 &e(Running)");
        profile.sendMessage("&8[&3Xime&8] &fCurrent game types&8: &eSurvival Games, Survival Games Maker");
        profile.sendMessage("&8[&3Xime&8] &fRunning on&8: &eEU Server &8[&eFrankfurt, Germany&8]");
        profile.sendMessage("&8[&3Xime&8] &fVersion&8: &e" + profile.getLanguage().getVersion());
        profile.sendMessage("&8[&3Xime&8] &fBuild type&8: &eAlpha Wave");
        profile.sendMessage("&8[&3Xime&8] &fUpdate channel&8: &eDevelopment");

        if(args.length > 0 && args[0].equalsIgnoreCase("reload") && player.hasPermission("xime.admin")) {
            plugin.reloadConfig();
            plugin.getLanguageHandler().load();
            plugin.getMapHandler().load();

            profile.sendMessage("&8[&3Xime&8] &aReloaded config & map data");
        }

        return true;
    }
}