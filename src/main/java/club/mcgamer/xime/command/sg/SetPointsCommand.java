package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SetPointsCommand extends XimeCommand {

    public SetPointsCommand() {
        super("setpoints");
        this.description = "Bounty a player";
        this.usageMessage = "/setpoints <player> <amount>";
        this.setAliases(Collections.emptyList());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;
        SGServerable serverable = (SGServerable) profile.getServerable();
        String prefix = serverable.getPrefix();

        if (!hasArgs(sender, args, 2)) return true;

        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;

        int argumentPoints;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);
        PlayerData argumentData = argumentProfile.getPlayerData();


        try {
            argumentPoints = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            hasArgs(sender, args, Short.MAX_VALUE);
            return true;
        }

        if (argumentPoints < 0) {
            profile.sendMessage(prefix + "&cYou cannot set negative points!");
            return true;
        }

        argumentData.setSgPoints(argumentPoints);
        profile.sendMessage(prefix + String.format("&fYou have set &f%s&f's points to &8[&e%s&8]",
                argumentProfile.getDisplayName(),
                argumentPoints));
        return true;
    }

}
