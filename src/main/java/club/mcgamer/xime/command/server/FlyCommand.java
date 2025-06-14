package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class FlyCommand extends XimeCommand {

    public FlyCommand() {
        super("fly");
        this.description = "Fly in hubs and lobbies";
        this.usageMessage = "/fly";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.platinum");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        PlayerData generalData = profile.getPlayerData();
        String prefix = profile.getServerable().getPrefix();

        generalData.setCanFly(!generalData.isCanFly());

        if ((profile.getServerable() instanceof SGServerable serverable) && serverable.getGameState() != GameState.LOBBY) {
            profile.sendMessage(prefix + "&cYou cannot use this command right now.");
            return true;
        }
        if ((profile.getServerable() instanceof BGServerable)) {
            profile.sendMessage(prefix + "&cYou cannot use this command right now.");
            return true;
        }

        player.setAllowFlight(generalData.isCanFly());
        profile.sendMessage(String.format(prefix + "&fYou can %s fly&8.", generalData.isCanFly() ? "now" : "no longer"));
        return true;
    }
}