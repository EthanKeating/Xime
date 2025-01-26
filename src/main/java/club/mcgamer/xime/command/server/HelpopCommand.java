package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.server.Serverable;
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

public class HelpopCommand extends XimeCommand {

    public HelpopCommand() {
        super("helpop");
        this.description = "helpop";
        this.usageMessage = "/helpop";
        this.setAliases(new ArrayList<String>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        Serverable serverable = profile.getServerable();

        String prefix = serverable.getPrefix();

        if (serverable instanceof HubServerable) {
            profile.sendMessage(prefix + "&6&m                 &b&l Global Help &6&m                 ")
                    .sendMessage(prefix + "&6/ghostfix&8: &7To stop being stuck in the ground.")
                    .sendMessage(prefix + "&6/join [gamemode] [id]&8: &7Join a game.")
                    .sendMessage(prefix + "&6/watch [gamemode] [id]&8: &7Watch a game.")
                    .sendMessage(prefix + "&6/ping [player]&8: &7Shows your strength to the server.")
                    .sendMessage(prefix + "&6/report [player] [rule] [desc]&8: &7Reports a player.")
                    .sendMessage(prefix + "&6/request [message]&8: &7Send a message to staff.")
                    .sendMessage(prefix + "&6/sgmaker&8: &7Opens up SGMaker GUI to create custom games.")
                    .sendMessage(prefix + "&6/sidebar&8: &7Choose scoreboard preference.");
            return true;
        }

        if (serverable instanceof SGServerable) {
            profile.sendMessage(prefix + "&6 /bounty [player] [amount]&8:&7 Place a bounty on a player.")
            .sendMessage(prefix + "&6 /confirmbounty&8:&7 To confirm you want to bounty.")
            .sendMessage(prefix + "&6 /hub&8:&7 Sends you to the hub server.")
                    .sendMessage(prefix + "&6 /infomation&8:&7 Shows game/server infomation.")
                    .sendMessage(prefix + "&6 /kill&8:&7 Quick way of dying.")
                    .sendMessage(prefix + "&6 /leaderboards&8:&7 Lists the top 10 players of the network.")
                    .sendMessage(prefix + "&6 /list&8:&7 Shows all the players in the game.")
                    .sendMessage(prefix + "&6 /message [player] [message]&8:&7 Message a player ingame.")
                    .sendMessage(prefix + "&6 /reply [message]&8:&7 Reply to an ingame message.")
                    .sendMessage(prefix + "&6 /spectate&8:&7 Teleport to a tribute.")
                    .sendMessage(prefix + "&6 /sponsor [player]&8:&7 Gift a player in game items.")
                    .sendMessage(prefix + "&6 /statistics [player]&8:&7 Shows players statistics.")
                    .sendMessage(prefix + "&6 /timeleft&8:&7 Shows the remaining time.")
                    .sendMessage(prefix + "&6 /vote [id]&8:&7 Shows the map avaiable to play in lobby.");
            return true;
        }

        if (serverable instanceof BGServerable) {
            profile
                    .sendMessage(prefix + "&6 /hub&8:&7 Sends you to the hub server.")
                    .sendMessage(prefix + "&6 /infomation&8:&7 Shows game/server infomation.")
                    .sendMessage(prefix + "&6 /kill&8:&7 Quick way of dying.")
                    .sendMessage(prefix + "&6 /list&8:&7 Shows all the players in the game.")
                    .sendMessage(prefix + "&6 /message [player] [message]&8:&7 Message a player ingame.")
                    .sendMessage(prefix + "&6 /reply [message]&8:&7 Reply to an ingame message.")
                    .sendMessage(prefix + "&6 /statistics [player]&8:&7 Shows players statistics.")
                    .sendMessage(prefix + "&6 /timeleft&8:&7 Shows the remaining time.");
            return true;
        }

        return true;
    }
}