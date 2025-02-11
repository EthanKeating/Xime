package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.impl.ReplyData;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCommand extends XimeCommand {

    public ReplyCommand() {
        super("reply");
        this.description = "send a private message";
        this.usageMessage = "/reply <private message...>";
        this.setAliases(Arrays.asList("r"));

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if(!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);
        ReplyData replyData = profile.getReplyData();
        String prefix = profile.getServerable().getPrefix();

        CooldownData cooldownData = profile.getCooldownData();
        if (cooldownData.hasMessageCooldown(0.5)) return true;

        if (!replyData.isActive()) {
            profile.sendMessage("&8[&3Xime&8] &cYou have nobody to reply to!");
            return true;
        }

        Profile replyProfile = plugin.getProfileHandler().getProfile(replyData.getReplyUUID());
        if (replyProfile == null) {
            profile.sendMessage(prefix + "&cYou have nobody to reply to!");
            return true;
        }

        //TODO: Check if pms are disabled for argumentProfile

        profile.getReplyData().setReplyUUID(replyProfile.getUuid());
        profile.getReplyData().setReplyTimestamp(System.currentTimeMillis());

        replyProfile.getReplyData().setReplyUUID(profile.getUuid());
        replyProfile.getReplyData().setReplyTimestamp(System.currentTimeMillis());

        String message = String.join(" ", args);
        replyProfile.sendMessageRaw(TextUtil.translate(String.format("&8[&bFrom &f%s&8] &7", profile.getDisplayName())) + message);
        profile.sendMessageRaw(TextUtil.translate(String.format("&8[&bTo &f%s&8] &7", replyProfile.getDisplayName())) + message);

        cooldownData.setMessageCooldown();

        return true;
    }
}
