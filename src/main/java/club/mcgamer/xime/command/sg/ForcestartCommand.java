package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.settings.GameSettings;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.runnable.MakerLobbyRunnable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ForcestartCommand extends XimeCommand {
    public ForcestartCommand() {
        super("forcestart");
        this.description = "Forcestart your current game";
        this.usageMessage = "/forcestart";
        this.setAliases(Arrays.asList("fs"));
        setPermission("xime.staff");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;
        if (!hasPermission(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        String prefix = serverable.getPrefix();

        if (serverable.getGameState() != GameState.LOBBY) {
            sender.sendMessage(TextUtil.translate(prefix + "&cYou can only use this command in the lobby."));
            return true;
        }

        if (serverable instanceof SGMakerServerable) {
            ((MakerLobbyRunnable) serverable.getCurrentRunnable()).setStartGame(true);
        }

        GameTimer gameTimer = serverable.getGameTimer();
        GameSettings gameSettings = serverable.getGameSettings();

        gameTimer.setTime(10);
        gameSettings.setMinimumPlayers(1);

        serverable.announce(String.format("&4The game has been force started by %s&8.", profile.getDisplayName()));

        return true;
    }
}
