package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
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
        if (cooldownData.hasAnnounceCooldown(getCooldownLength(player))) return true;

        if (profile.getServerable() instanceof SGMakerServerable sgMakerServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&l&nClick Here!", profile.getDisplayNameBypassDisguise(), sgMakerServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/secret " + sgMakerServerable.getSecret()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate(String.format("&fClick to join &8[&6EU%s&8]", sgMakerServerable.toString()))) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.getPlayer().spigot().sendMessage(message);
            });
            cooldownData.setAnnounceCooldown();

            return true;
        }

        if (profile.getServerable() instanceof SGServerable sgServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&l&nClick Here!", profile.getDisplayNameBypassDisguise(), sgServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sg " + sgServerable.getServerId()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate(String.format("&fClick to join &8[&6EU%s&8]", sgServerable.toString()))) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.getPlayer().spigot().sendMessage(message);
            });
            cooldownData.setAnnounceCooldown();

            return true;
        }

        if (profile.getServerable() instanceof BGServerable bgServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&lClick Here!", profile.getDisplayNameBypassDisguise(), bgServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join bg " + bgServerable.getServerId()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate("&aClick to join " + TextUtil.translate(String.format("&fClick to join &8[&6EU%s&8]", bgServerable.toString())))) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.getPlayer().spigot().sendMessage(message);
            });
            cooldownData.setAnnounceCooldown();

            return true;
        }

        profile.sendMessage(profile.getServerable().getPrefix() + "&cYou cannot use this command right now.");

        return true;
    }

    private double getCooldownLength(Player player  ) {
        if (false) {
            System.out.println("how tf");
        } else if (player.hasPermission("xime.admin")) {
            return 10.0;
        } else if (player.hasPermission("xime.staff")) {
            return 30.0;
        } else if (player.hasPermission("xime.quantum")) {
            return 60.0;
        } else if (player.hasPermission("xime.platinum")) {
            return 90.0;
        }
        return 120.0;
    }
}