package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScatterCommand extends XimeCommand {

    //Unused command for stress testing
    public ScatterCommand() {
        super("scatter");
        this.description = "Scatter command";
        this.usageMessage = "/scatter";
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

        for(Profile loopProfile : plugin.getProfileHandler().getProfiles().stream().filter(loopProfile -> loopProfile.getServerable() != null).filter(loopProfile -> loopProfile.getServerable() instanceof HubServerable).collect(Collectors.toCollection(ArrayList::new))) {
            List<SGServerable> servers = new ArrayList<>(plugin.getServerHandler().getByClass(SGServerable.class).stream().map(serverable -> (SGServerable) serverable).toList());

            servers.stream().min(Comparator.comparingInt(serverable -> serverable.getPlayerList().size())).get().add(loopProfile);
        }
        profile.sendMessage("&8[&3Xime&8] &fScattered players to SG servers");

        return true;
    }
}