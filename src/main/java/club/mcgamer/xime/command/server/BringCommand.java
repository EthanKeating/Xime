package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BringCommand extends XimeCommand {

    public BringCommand() {
        super("bring");
        this.description = "bring command";
        this.usageMessage = "/bring";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!isPlayer(sender)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(),
                BGServerable.class,
                SGServerable.class,
                SGMakerServerable.class)) return true;

        List<Profile> profileList = plugin.getProfileHandler().getProfiles()
                .stream()
                .filter(loopProfile -> loopProfile.getServerable() != null)
                .filter(loopProfile -> loopProfile.getServerable() instanceof HubServerable)
                .toList();

        for(Profile loopProfile : profileList) {
            if (profile.getServerable().isFull())
                break;

            profile.getServerable().add(loopProfile);
        }
        profile.sendMessage("&8[&3Xime&8] &fBringing all hub players to your game");
        return true;
    }
}