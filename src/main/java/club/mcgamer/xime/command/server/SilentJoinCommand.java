package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class SilentJoinCommand extends XimeCommand {

    public SilentJoinCommand() {
        super("silentjoin");
        this.description = "Toggle silent join";
        this.usageMessage = "/silentjoin";
        this.setAliases(Arrays.asList("sj"));
        setPermission("xime.diamond");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        PlayerData playerData = profile.getPlayerData();

        playerData.setSilentJoin(!playerData.isSilentJoin());
        profile.sendMessage("&8[&3Xime&8] &fSilent join has been " + (playerData.isSilentJoin() ? "enabled" : "disabled") + "&8.");

        return true;
    }
}