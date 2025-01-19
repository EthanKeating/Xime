package club.mcgamer.xime.command.map;


import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ItemsCommand extends XimeCommand {

    public ItemsCommand() {
        super("items");
        this.description = "Get the build tools";
        this.usageMessage = "/items";
        this.setAliases(new ArrayList<>());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!hasPermission(sender)) return true;
        if (!isPlayer(sender)) return true;

        String map = args[0];
        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);

        if (!isCorrectServerable(sender, profile.getServerable(), BuildServerable.class)) return true;

        BuildServerable serverable = (BuildServerable) profile.getServerable();
        serverable.mainItems(profile.getPlayer());

        return true;
    }
}
