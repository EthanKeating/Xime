package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SetResetTokensCommand extends XimeCommand {

    public SetResetTokensCommand() {
        super("addresettoken");
        this.description = "Add a reset token to a player";
        this.usageMessage = "/addresettoken <player> <amount>";
        this.setAliases(Collections.emptyList());
        setPermission("xime.admin");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (isPlayer(sender) && !hasPermission(sender)) return true;
        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;
        SGServerable serverable = (SGServerable) profile.getServerable();
        String prefix = serverable.getPrefix();

        if (!hasArgs(sender, args, 2)) return true;

        Player argumentPlayer = isPlayer(sender, args[0]);
        PlayerData argumentData;
        if (argumentPlayer == null) {
            argumentData = plugin.getDataHandler().getPlayerData(args[0]);
        } else {
            Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);
            argumentData = argumentProfile.getPlayerData();
        }

        argumentData.setStatResetTokens(argumentData.getStatResetTokens() + 1);
        if (argumentPlayer == null) plugin.getDataHandler().updatePlayerData(argumentData);
        profile.sendMessage(prefix + String.format("&fYou have updated &f%s&f's stat reset tokens to &8[&e%s&8]",
                argumentData.getDisplayName(),
                argumentData.getStatResetTokens()));
        return true;
    }

}
