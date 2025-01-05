package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.menu.sg.SponsorMenu;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

        if(serverable.getTributeList().contains(profile) || serverable.getGameState() == GameState.LOBBY
                || serverable.getGameState() == GameState.LOADING
                || serverable.getGameState() == GameState.PREGAME
                || serverable.getGameState() == GameState.CLEANUP
                || serverable.getGameState() == GameState.RESTARTING) {
            profile.sendMessage("&8[&6MCSG&8] &cYou cannot use this command right now.");
            return true;
        }

        if (!hasArgs(sender, args, 1, "&8[&6MCSG&8] &c")) return true;
        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (!serverable.getPlayerList().contains(argumentProfile)) {
            profile.sendMessage("That player is not online!");
            return true;
        }

        if (!serverable.getTributeList().contains(argumentProfile)) {
            profile.sendMessage("&8[&6MCSG&8] &cYou cannot sponsor that player!");
            return true;
        }

        new SponsorMenu(profile, argumentProfile, serverable).open(player);
        //TODO: Check if player has enough points
        //&8[&6MCSG&8] &4You do not have enough points&8.

        return true;
    }

}
