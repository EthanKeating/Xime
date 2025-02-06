package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.staff.StaffServerable;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InviteCommand extends XimeCommand {

    public InviteCommand() {
        super("invite");
        this.description = "Invite a player to your maker game";
        this.usageMessage = "/invite [player]";
        this.setAliases(new ArrayList<>());

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!isPlayer(sender)) return true;
        if (!hasArgs(sender, args, 1)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if(!isCorrectServerable(sender, profile.getServerable(), SGMakerServerable.class)) return true;

        CooldownData cooldownData = profile.getCooldownData();
        if (cooldownData.hasInviteCooldown(0.5)) return true;

        SGMakerServerable serverable = (SGMakerServerable) profile.getServerable();
        Player argumentPlayer = isPlayer(sender, args[0]);

        if (argumentPlayer == null) return true;
        Profile argumentProfile = plugin.getProfileHandler().getProfile(argumentPlayer);

        if (argumentProfile.getServerable() == profile.getServerable()) {
            profile.sendMessage("&8[&3Xime&8] &cThat player is already on the server!");
            return true;
        }

        profile.sendMessage(String.format("&8[&3Xime&8] &a%s &ahas been invited to the server!", argumentProfile.getDisplayName()));
        TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&3Xime&8] &6You have been invited to join %s&6's private game! &a&lCLICK HERE", serverable.getOwner().getDisplayNameBypassDisguise())));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                new TextComponent(TextUtil.translate("&aClick to join the server!")),
        }));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/secret " + serverable.getSecret()));
        argumentPlayer.spigot().sendMessage(message);
        cooldownData.setInviteCooldown();

        return true;
    }
}
