package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InfoCommand extends XimeCommand {

    public InfoCommand() {
        super("xime");
        this.description = "Xime info command";
        this.usageMessage = "/xime";
        this.setAliases(new ArrayList<String>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isPlayer(sender)) return true;

        Serverable serverable = profile.getServerable();

        profile.sendMessage(String.format("&6- Server &3%s &6Info -", serverable.toString()));
        profile.sendMessage("Players&8: &f" + serverable.getPlayerList());
        profile.sendMessage("Current Map&8: &f" + serverable.getMapData().getMapName());
        profile.sendMessage("Map Creator&8: &f" + serverable.getMapData().getMapAuthor());
        profile.sendMessage("Map Link&8: &f" + serverable.getMapData().getMapLink());
        profile.sendMessage("Developers&8: &4E&ct&6h&es");
        profile.sendMessage("&fHosted by&8: &ehttps://www.mcgamer.club/");
        profile.sendMessage(String.format("&fRunning the&8: &8[&3Xime&8] &cv%s &fengine&8.", ServerHandler.SERVER_VERSION));

        return true;
    }
}