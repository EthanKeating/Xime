package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeam;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeamChatCommand extends XimeCommand {

    public TeamChatCommand() {
        super("teamchat");
        this.description = "send a team message";
        this.usageMessage = "/teamchat <message>";
        this.setAliases(Arrays.asList("tc"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        SGTeamProvider teamProvider = serverable.getGameSettings().getTeamProvider();

        if (teamProvider.getTeamType() == TeamType.NO_TEAMS) {
            profile.sendMessage(serverable.getPrefix() + "&cYou cannot use this command right now.");
            return true;
        }

        if (teamProvider.getTeamOriginal(profile) == null) {
            profile.sendMessage(serverable.getPrefix() + "&cYou must be in a team to use this command.");
            return true;
        }

        SGTeam team = teamProvider.getTeamOriginal(profile);
        String message = String.join(" ", args);

        team.getEveryPlayer().forEach(loopProfile -> {
            if (loopProfile.getPlayer() != null) {
                if (loopProfile.getServerable() != null && loopProfile.getServerable() == profile.getServerable())
                    loopProfile.sendMessageRaw(TextUtil.translate(String.format("&a(Team) &2%s&8: &f", profile.getDisplayName())) + message);
            }
        });

        return true;
    }
}
