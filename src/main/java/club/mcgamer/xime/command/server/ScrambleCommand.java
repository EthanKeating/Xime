package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ScrambleCommand extends XimeCommand {

    public ScrambleCommand() {
        super("scramble");
        this.description = "Scramble your stats";
        this.usageMessage = "/scramble";
        this.setAliases(Collections.emptyList());
        setPermission("xime.diamond");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        String prefix = profile.getServerable().getPrefix();

        if (profile.getServerable() instanceof SGServerable serverable && serverable.getGameState() != GameState.LOBBY) {
            profile.sendMessage(prefix + "&4You cannot use this command right now&8.");
            return true;
        }

        if (profile.getDisguiseData() != null) {
            profile.getDisguiseData().setMockData(PlayerData.createMock(profile));
            profile.sendMessage(prefix + "&fYour stats have been re-scrambled.");
        } else {
            profile.setDisguiseData(new DisguiseData(profile, profile.getUuid(), profile.getName(), profile.getSkin(), profile.getRank()));
            profile.getDisguiseData().setMockData(PlayerData.createMock(profile));
            profile.sendMessage(prefix + "&fYour stats have been scrambled.");
        }

        return true;
    }
}