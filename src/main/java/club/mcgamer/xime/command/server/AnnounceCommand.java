package club.mcgamer.xime.command.server;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.command.XimeCommand;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.profile.data.temporary.CooldownData;
import club.mcgamer.xime.server.ServerHandler;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.util.TextUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnounceCommand extends XimeCommand {

    private static final Map<String, Double> permissionCooldowns = Map.of(
            "xime.admin", 5.0,
            "xime.staff", 10.0,
            "xime.quantum", 10.0,
            "xime.platinum", 15.0,
            "xime.diamond", 20.0
    );

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
            if (sgMakerServerable.getGameState() != GameState.LOBBY && sgMakerServerable.getGameState() != GameState.PREGAME) {
                profile.sendMessage(sgMakerServerable.getPrefix() + "&cYou cannot use this command right now.");
                return true;
            }

            if (sgMakerServerable.getPrivacyMode() == PrivacyMode.PRIVATE) {
                profile.sendMessage(sgMakerServerable.getPrefix() + "&cYou cannot announce a private server.");
                return true;
            }

            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&l&nClick Here!", profile.getDisplayName(), sgMakerServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sgmaker " + sgMakerServerable.getServerId()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate(String.format("&fClick to join &8[&6EU%s&8]", sgMakerServerable.toString()))) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.getPlayer().spigot().sendMessage(message);
            });
            cooldownData.setAnnounceCooldown();
            return true;
        }

        if (profile.getServerable() instanceof SGServerable sgServerable) {
            if (sgServerable.getGameState() != GameState.LOBBY && sgServerable.getGameState() != GameState.PREGAME) {
                profile.sendMessage(sgServerable.getPrefix() + "&cYou cannot use this command right now.");
                return true;
            }

            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&l&nClick Here!", profile.getDisplayName(), sgServerable)));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/join sg " + sgServerable.getServerId()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(TextUtil.translate(String.format("&fClick to join &8[&6EU%s&8]", sgServerable.toString()))) }));
            plugin.getProfileHandler().getProfiles().forEach(loopProfile -> {
                loopProfile.getPlayer().spigot().sendMessage(message);
            });
            cooldownData.setAnnounceCooldown();
            return true;
        }

        if (profile.getServerable() instanceof BGServerable bgServerable) {
            TextComponent message = new TextComponent(TextUtil.translate(String.format("&8[&eMCGamer&8] &f%s &fwould like you to join &8[&6EU%s&8] &f&lClick Here!", profile.getDisplayName(), bgServerable)));
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

    private double getCooldownLength(Player player) {
        for (Map.Entry<String, Double> entry : permissionCooldowns.entrySet())
            if (player.hasPermission(entry.getKey()))
                return entry.getValue();

        return 60.0;
    }
}