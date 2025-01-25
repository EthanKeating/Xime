package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.menu.sg.SponsorMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SponsorCommand extends XimeCommand {

    public SponsorCommand() {
        super("sponsor");
        this.description = "Sponsor a player";
        this.usageMessage = "/sponsor <player>";
        this.setAliases(Collections.emptyList());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        String prefix = serverable.getPrefix();

        if(serverable.getTributeList().contains(profile) || serverable.getGameState() == GameState.LOBBY
                || serverable.getGameState() == GameState.LOADING
                || serverable.getGameState() == GameState.PREGAME
                || serverable.getGameState() == GameState.CLEANUP
                || serverable.getGameState() == GameState.ENDGAME
                || serverable.getGameState() == GameState.RESTARTING) {
            profile.sendMessage(prefix + "&cYou cannot use this command right now.");
            return true;
        }

        if (!hasArgs(sender, args, 1)) return true;
        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (!serverable.getPlayerList().contains(argumentProfile)) {
            profile.sendMessage(prefix + "&cThat player is not online!");
            return true;
        }

        if (!serverable.getTributeList().contains(argumentProfile)) {
            profile.sendMessage(prefix + "&cYou cannot sponsor that player!");
            return true;
        }

        new SponsorMenu(profile, argumentProfile, serverable).open(player);

        return true;
    }

}
