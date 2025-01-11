package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class SecretCommand extends XimeCommand {

    public SecretCommand() {
        super("secret");
        this.description = "Secret key command to join private games";
        this.usageMessage = "/secret <key>";
        this.setAliases(Arrays.asList("secret", "code", "secretkey", "key"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (!(profile.getServerable() instanceof HubServerable)) {
            profile.sendMessage("&8[&3Xime&8] &cYou can only execute this command in the hub!");
            return true;
        }

        Optional<SGMakerServerable> serverableOptional = plugin.getServerHandler().getServerList()
                .stream()
                .filter(loopServerable -> loopServerable instanceof SGMakerServerable)
                .map(loopServerable -> (SGMakerServerable) loopServerable)
                .filter(loopServerable -> loopServerable.getSecret().equalsIgnoreCase(args[0]))
                .findFirst();

        if (serverableOptional.isPresent()) {
            SGMakerServerable serverable = serverableOptional.get();

            profile.sendMessage(String.format("&8[&3Xime&8] &fConnecting you to %s&f's custom game...", serverable.getOwner().getDisplayNameBypassDisguise()));
            serverable.add(profile);
            return true;
        }

        profile.sendMessage("&8[&3Xime&8] &cCould not find a server with that secret key!");
        return true;
    }

}
