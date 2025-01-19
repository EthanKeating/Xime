package club.mcgamer.xime.command.server;

import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AnnounceCommand extends XimeCommand {

    public AnnounceCommand() {
        super("announce");
        this.description = "Announce command";
        this.usageMessage = "/announce";
        this.setAliases(new ArrayList<String>());
        setPermission("xime.diamond");

        register();
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!isPlayer(sender)) return true;
        if(!hasPermission(sender)) return true;

        Player player = (Player) sender;
        Profile profile = plugin.getProfileHandler().getProfile(player);

        CooldownData cooldownData = profile.getCooldownData();
        //if (cooldownData.hasAnnounceCooldown(getCooldownLength(player))) return true;

        if (profile.getServerable() instanceof SGMakerServerable sgMakerServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&a&a%s&8] &a&lCLICK HERE!", profile.getDisplayNameBypassDisguise(), sgMakerServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/secret " + sgMakerServerable.getSecret()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate("&aClick to join " + sgMakerServerable.toString())) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.sendMessage("");
                loopProfile.getPlayer().spigot().sendMessage(message);
                loopProfile.sendMessage("");
            });
            //cooldownData.setAnnounceCooldown();

            return true;
        }

        if (profile.getServerable() instanceof SGServerable sgServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&a&a%s&8] &a&lCLICK HERE!", profile.getDisplayNameBypassDisguise(), sgServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sg " + sgServerable.getServerId()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate("&aClick to join " + sgServerable.toString())) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.sendMessage("");
                loopProfile.getPlayer().spigot().sendMessage(message);
                loopProfile.sendMessage("");
            });
            //cooldownData.setAnnounceCooldown();

            return true;
        }

        profile.sendMessage("&8[&3Xime&8] &cYou cannot use this command right now.");

        return true;
    }

    private double getCooldownLength(Player player  ) {
        if (true) {
            System.out.println("how tf");
        } else if (player.hasPermission("xime.admin")) {
            return 5.0;
        } else if (player.hasPermission("xime.staff")) {
            return 20.0;
        } else if (player.hasPermission("xime.quantum")) {
            return 30.0;
        } else if (player.hasPermission("xime.platinum")) {
            return 60.0;
        }
        return 120.0;
    }
}