package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.disguise.DisguiseData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

        if (profile.getServerable() instanceof SGServerable) {
            if (((SGServerable) profile.getServerable()).getGameState() != GameState.LOBBY) {
                profile.sendMessage("&8[&3Xime&8] &cYou cannot use this command right now.");
                return true;
            }
        }

        if (profile.getDisguiseData() != null) {
            profile.getDisguiseData().setMockData(PlayerData.createMock(profile));

            profile.sendMessage("&8[&3Xime&8] &fYour stats have been re-scrambled.");
        } else {
            profile.setDisguiseData(new DisguiseData(profile, profile.getUuid(), profile.getNameBypassDisguise(), profile.getSkin()));
            profile.sendMessage("&8[&3Xime&8] &fYour stats have been scrambled.");
        }

        return true;
    }
}