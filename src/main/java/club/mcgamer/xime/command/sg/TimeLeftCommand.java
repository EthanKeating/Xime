package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sg.timer.GameTimer;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TimeLeftCommand extends XimeCommand {
    public TimeLeftCommand() {
        super("timeleft");
        this.description = "View the time left in a state";
        this.usageMessage = "/timeleft";
        this.setAliases(Arrays.asList("tl"));

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

        GameState gameState = serverable.getGameState();
        GameTimer gameTimer = serverable.getGameTimer();

        String minutesPlural = gameTimer.getMinutes() == 1 ? "minute" : "minutes";
        String secondsPlural = gameTimer.getSeconds() == 1 ? "second" : "seconds";

        String minutesPortion = gameTimer.getMinutes() == 0 ? "" : String.format("&8[&e%s&8] &f%s&8,&f", gameTimer.getMinutes(), minutesPlural);
        String secondsPortion = String.format("&8[&e%s&8] &f%s", gameTimer.getSeconds(), secondsPlural);

        profile.sendMessage(TextUtil.translate(String.format(prefix + "&fThere is currently %s %s left in %s&8.",
                minutesPortion,
                secondsPortion,
                gameState.getName())));
        return true;
    }
}
