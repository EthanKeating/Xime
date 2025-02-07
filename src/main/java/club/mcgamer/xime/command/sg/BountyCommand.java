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

public class BountyCommand extends XimeCommand {

    public BountyCommand() {
        super("bounty");
        this.description = "Bounty a player";
        this.usageMessage = "/bounty <player> <amount>";
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

        if (serverable.getTributeList().contains(profile)
                || (serverable.getGameState() != GameState.LIVEGAME)) {
            profile.sendMessage(prefix + "&cYou cannot use this command right now.");
            return true;
        }

        if (!hasArgs(sender, args, 2)) return true;

        Player argumentPlayer = isPlayer(sender, args[0]);
        if (argumentPlayer == null) return true;

        int argumentPoints;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);
        PlayerData playerData = profile.getPlayerData();

        if (!serverable.getPlayerList().contains(argumentProfile)) {
            profile.sendMessage(prefix + "&cThat player is not online!");
            return true;
        }

        if (!serverable.getTributeList().contains(argumentProfile)
                || !(argumentProfile.getTemporaryData() instanceof SGTemporaryData argumentTemporaryData)) {
            profile.sendMessage(prefix + "&cYou cannot bounty that player!");
            return true;
        }

        try {
            argumentPoints = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            hasArgs(sender, args, Short.MAX_VALUE);
            return true;
        }

        if (argumentPoints < 10) {
            profile.sendMessage(prefix + "&cBounties must be higher than &8[&e10&8] &cpoints&8.");
            return true;
        }
        if (playerData.getSgPoints() < argumentPoints) {
            profile.sendMessage(prefix + "&4You do not have enough points&8.");
            return true;
        }

        playerData.setSgPoints(playerData.getSgPoints() - argumentPoints);
        playerData.setSgBountiesSent(playerData.getSgBountiesSent() + 1);
        argumentProfile.getPlayerData().setSgBountiesReceived(argumentProfile.getPlayerData().getSgBountiesReceived() + 1);
        argumentTemporaryData.setBounty(argumentTemporaryData.getBounty() + argumentPoints);
        serverable.announce(String.format("&3Bounty has been set on &f%s &3by &f%s&3 for &8[&e%s&8] &3points.",
                argumentProfile.getDisplayName(),
                profile.getDisplayName(),
                argumentPoints));
        return true;
    }

}
