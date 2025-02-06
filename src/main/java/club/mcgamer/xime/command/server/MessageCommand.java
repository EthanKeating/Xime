package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MessageCommand  extends XimeCommand {

    public MessageCommand() {
        super("msg");
        this.description = "send a private message";
        this.usageMessage = "/message <player> <private message...>";
        this.setAliases(Arrays.asList("pm", "message", "w", "m", "whisper", "tell"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 2)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        Player argumentPlayer = Bukkit.getPlayer(args[0]);

        CooldownData cooldownData = profile.getCooldownData();
        if (cooldownData.hasMessageCooldown(0.5)) return true;

        if (argumentPlayer == null) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat player is not online."));
            return true;
        }

        if (argumentPlayer == player) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cYou cannot message yourself!"));
            return true;
        }

        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (argumentProfile.getDisguiseData() != null && !args[0].equalsIgnoreCase(argumentProfile.getDisguiseData().getName())) {
            sender.sendMessage(TextUtil.translate("&8[&3Xime&8] &cThat player is not online."));
            return true;
        }

        //TODO: Check if pms are disabled for argumentProfile

        profile.getReplyData().setReplyUUID(argumentProfile.getUuid());
        profile.getReplyData().setReplyTimestamp(System.currentTimeMillis());

        argumentProfile.getReplyData().setReplyUUID(profile.getUuid());
        argumentProfile.getReplyData().setReplyTimestamp(System.currentTimeMillis());

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        argumentProfile.sendMessage(String.format("&8[&bFrom &f%s&8] &7%s", profile.getDisplayName(), message));
        profile.sendMessage(String.format("&8[&bTo &f%s&8] &7%s", argumentProfile.getDisplayName(), message));

        cooldownData.setMessageCooldown();

        return true;
    }
}
