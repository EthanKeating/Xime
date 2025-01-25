package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpectateCommand extends XimeCommand {

    public SpectateCommand() {
        super("spectate");
        this.description = "Spectate a player";
        this.usageMessage = "/spectate [tribute]";
        this.setAliases(Arrays.asList("spec"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        String prefix = serverable.getPrefix();
        SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

        if (args.length > 0) {
            Player argumentPlayer =  isPlayer(sender, args[0]);

            if (argumentPlayer == null) return true;

            Profile spectatedProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

            profile.sendMessage(String.format(prefix + "&fTeleporting to %s", spectatedProfile.getDisplayName()));
            profile.getPlayer().teleport(spectatedProfile.getPlayer().getLocation().add(0.0, 0.25, 0.0));
            PlayerUtil.setFlying(profile);
            return true;
        }

        if (!temporaryData.canSpectate()) {
            return true;
        }

        if(!serverable.getSpectatorList().contains(profile)) {
            profile.sendMessage(prefix + "&cYou can only use this command as a spectator.");
            return true;
        }
        if (temporaryData.getPreviousSpectateIndex() >= serverable.getTributeList().size() - 1)
            temporaryData.setPreviousSpectateIndex(0);
        else
            temporaryData.setPreviousSpectateIndex(temporaryData.getPreviousSpectateIndex() + 1);

        if (serverable.getTributeList().isEmpty())
            return true;

        Profile spectatedProfile = serverable.getTributeList().get(temporaryData.getPreviousSpectateIndex());

        profile.sendMessage(String.format(prefix + "&fTeleporting to %s", spectatedProfile.getDisplayName()));
        profile.getPlayer().teleport(spectatedProfile.getPlayer().getLocation().add(0.0, 0.25, 0.0));
        temporaryData.setLastSpectate(System.currentTimeMillis());
        PlayerUtil.setFlying(profile);

        return true;
    }

}
