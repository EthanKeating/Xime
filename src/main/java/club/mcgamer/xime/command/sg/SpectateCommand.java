package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
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

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        SGTemporaryData temporaryData = (SGTemporaryData) profile.getTemporaryData();

        if (temporaryData.getPreviousSpectateIndex() > serverable.getTributeList().size() - 1)
            temporaryData.setPreviousSpectateIndex(0);

        if (serverable.getTributeList().isEmpty())
            return true;

        Profile spectatedProfile = serverable.getTributeList().get(temporaryData.getPreviousSpectateIndex());

        profile.sendMessage(String.format("&8[&6MCSG&8] &fTeleporting to %s", spectatedProfile.getDisplayName()));
        profile.getPlayer().teleport(spectatedProfile.getPlayer().getLocation().add(0.0, 0.25, 0.0));

        return true;
    }

}
