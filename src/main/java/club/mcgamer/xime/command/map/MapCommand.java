package club.mcgamer.xime.command.map;


import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.menu.build.MapEditorSelectionMenu;
import club.mcgamer.xime.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MapCommand extends XimeCommand {

    public MapCommand() {
        super("map");
        this.description = "Create a build server with a map to edit";
        this.usageMessage = "/map <map name>";
        this.setAliases(new ArrayList<>());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!hasPermission(sender)) return true;
        if (!isPlayer(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);

        if (args.length == 0) {
            new MapEditorSelectionMenu(profile, 1).open(profile.getPlayer());
            return true;
        }
        String map = args[0];
        World world = Bukkit.getWorld(map);

        if (world == null) {
            BuildServerable buildServerable = new BuildServerable();
            buildServerable.load(map, profile);
            return true;
        }
        plugin.getServerHandler()
                .getServerList()
                .stream()
                .filter(serverable -> serverable instanceof BuildServerable)
                .map(serverable -> (BuildServerable) serverable)
                .filter(serverable -> serverable.getWorld() == world)
                .findFirst()
                .ifPresent(buildServerable -> buildServerable.add(profile));

        return true;
    }
}
