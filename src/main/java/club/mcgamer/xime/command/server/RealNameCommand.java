package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class RealNameCommand extends XimeCommand {

    public RealNameCommand() {
        super("realname");
        this.description = "View a disguised player's real name";
        this.usageMessage = "/realname [player]";
        this.setAliases(Arrays.asList("rl", "identity", "realidentity", "ri"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        Player argumentPlayer = isPlayer(sender, args[0]);

        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (argumentProfile.getDisguiseData() == null) {
            profile.sendMessage("&8[&3Xime&8] &cThat player is using their real identity.");
            return true;
        }

        profile.sendMessage(String.format("&8[&3Xime&8] &f%s's real identity is %s&8.", argumentProfile.getDisplayName(), argumentProfile.getDisplayName()));

        return true;
    }
}
