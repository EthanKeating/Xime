package club.mcgamer.xime.command.sg;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.ProfileHandler;
import club.mcgamer.xime.profile.data.temporary.CombatTagData;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.event.ServerDeathEvent;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class KillCommand extends XimeCommand {
    public KillCommand() {
        super("kill");
        this.description = "Commit suicide";
        this.usageMessage = "/kill";
        this.setAliases(Arrays.asList("sepuku", "suicide", "kys"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!isPlayer(sender)) return true;

        Profile profile = plugin.getProfileHandler().getProfile((Player) sender);
        ServerHandler serverHandler = plugin.getServerHandler();

        if (!isCorrectServerable(sender, profile.getServerable(), SGServerable.class, SGMakerServerable.class)) return true;

        SGServerable serverable = (SGServerable) profile.getServerable();
        GameState gameState = serverable.getGameState();
        Player player = (Player) sender;

        if (serverable.getTributeList().contains(profile)) {
            switch (gameState) {
                case PREGAME:
                case LIVEGAME:
                case DEATHMATCH:
                case PREDEATHMATCH:
                    CombatTagData combatTagData = profile.getCombatTagData();
                    Optional<Profile> attackerOptional;

                    ProfileHandler profileHandler = plugin.getProfileHandler();

                    if (!combatTagData.isActive()
                            || combatTagData.getAttackedBy() == null
                            || profileHandler.getProfile(combatTagData.getAttackedBy()) == null
                            || profileHandler.getProfile(combatTagData.getAttackedBy()).getServerable() != serverable) {
                        attackerOptional = Optional.empty();
                    } else {
                        attackerOptional = Optional.of(profileHandler.getProfile(combatTagData.getAttackedBy()));
                    }
                    Bukkit.getPluginManager().callEvent(new ServerDeathEvent(
                            profile,
                            attackerOptional,
                            profile.getServerable(),
                            null
                    ));

                    profile.sendMessage("&8[&6MCSG&8] &fYou have committed suicide.");

                    return true;
            }
        }
        profile.sendMessage("&8[&6MCSG&8] &cYou can only use this command ingame.");

        return true;
    }
}
