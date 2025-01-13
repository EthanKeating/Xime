package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.map.MapPool;
import club.mcgamer.xime.map.VoteableMap;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTemporaryData;
import club.mcgamer.xime.sg.runnable.LobbyRunnable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class VoteCommand extends XimeCommand {
    public VoteCommand() {
        super("vote");
        this.description = "Vote for a map in the lobby";
        this.usageMessage = "/vote [id]";
        this.setAliases(Arrays.asList("v"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();

        if (serverable.getGameState() != GameState.LOBBY) {
            sender.sendMessage(TextUtil.translate("&8[&6MCSG&8] &cYou can only use this command in the lobby."));
            return true;
        }
        if (args.length == 0) {
            profile.sendMessage(String.format("&8[&6MCSG&8] &2Players waiting&8: &8[&6%s&8/&6%s&8]. &2Game requires &8[&6%s&8] &2to play.",
                    serverable.getPlayerList().size(),
                    serverable.getMaxPlayers(),
                    serverable.getGameSettings().getMinimumPlayers()));
            profile.sendMessage("&8[&6MCSG&8] &2Vote using &8[&a/vote #&8].");
            if (!serverable.getPreviousMapNames().isEmpty())
                profile.sendMessage("&8[&6MCSG&8] &2Previous maps played&8: &7" + String.join("&8, &7", serverable.getPreviousMapNames()) + "&8.");
            ((LobbyRunnable) serverable.getCurrentRunnable()).sendVotes(profile);
            return true;
        }

        MapPool mapPool = serverable.getMapPool();
        String pattern = String.format("^[1-%s]", mapPool.getRandomMaps().size());
        if (!args[0].matches(pattern)) {
            sender.sendMessage(TextUtil.translate(String.format("&8[&6MCSG&8] &cThere is no map with the number &e%s&8.", args[0])));
            return true;
        }

        int voteId = Integer.parseInt(args[0]);
        VoteableMap map = mapPool.getRandomMaps().get(voteId);

        if (mapPool.getVote(profile) == voteId) {
            mapPool.removeVote(profile);
            String votesPlural = map.getVotes() == 1 ? "vote" : "votes";

            sender.sendMessage(TextUtil.translate(String.format("&8[&6MCSG&8] &fYour map now has &8[&6%s&8] &f%s&8.", map.getVotes(), votesPlural)));
            return true;
        }

        mapPool.addVote(profile, voteId);
        String votesPlural = map.getVotes() == 1 ? "vote" : "votes";
        sender.sendMessage(TextUtil.translate(String.format("&8[&6MCSG&8] &fYour map now has &8[&6%s&8] &f%s&8.", map.getVotes(), votesPlural)));

        return true;
    }
}
