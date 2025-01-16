package club.mcgamer.xime.util;

import club.mcgamer.xime.profile.Profile;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

@UtilityClass
public class TextUtil {

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String stripColor(String text) {
        return ChatColor.stripColor(text);
    }
    public Pair<String, String> splitLine(String string) {
        String prefix = string, suffix = "";
        string = translate(string);

        if(string.length() > 16) {
            int splitAt = string.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
            prefix = string.substring(0, splitAt);
            suffix = ChatColor.getLastColors(prefix) + string.substring(splitAt);
            suffix = suffix.substring(0, Math.min(suffix.length(), 16));
        }

        return new Pair<>(translate(prefix), translate(suffix));
    }

    public static String toRainbow(String input) {
        StringBuilder rainbowString = new StringBuilder();

        ChatColor[] rainbowColors = {
                ChatColor.DARK_RED, ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
                ChatColor.GREEN, ChatColor.DARK_GREEN, ChatColor.AQUA, ChatColor.DARK_AQUA,
                ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE
        };

        for (int i = 0; i < input.length(); i++) {
            ChatColor color = rainbowColors[i % rainbowColors.length];
            rainbowString.append(color).append(ChatColor.BOLD).append(input.charAt(i));
        }

        return rainbowString.toString();
    }

    public static String toPascalCase(String string) {
        String replaced = string.replace('_', ' ');

        String[] words = replaced.split(" ");
        StringBuilder pascalCase = new StringBuilder();

        for (String word : words) {
            pascalCase.append(word.substring(0, 1).toUpperCase());
            pascalCase.append(word.substring(1).toLowerCase());
            pascalCase.append(" ");
        }
        return pascalCase.toString().trim();
    }

    public static void sendStaffMessage(Profile sendTo, Profile sender, String originalMessage) {
        if (sendTo.getPlayer().hasPermission("xime.staff")) {
            originalMessage = originalMessage.replace("%player%", sender.getDisguiseData() == null ? translate(sender.getDisplayName()) : translate(sender.getDisplayName() + "&8(" + sender.getDisplayNameBypassDisguise() + "&8)"));

            TextComponent message = new TextComponent(TextUtil.translate("&8["));

            TextComponent historyPortion = new TextComponent(TextUtil.translate("&6H"));
            historyPortion.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/history " + sender.getName()));
            message.addExtra(historyPortion);

            TextComponent kickPortion = new TextComponent(TextUtil.translate("&3K"));
            kickPortion.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kick " + sender.getName()));
            message.addExtra(kickPortion);

            TextComponent mutePortion = new TextComponent(TextUtil.translate("&2M"));
            mutePortion.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mute " + sender.getName()));
            message.addExtra(mutePortion);

            TextComponent banPortion = new TextComponent(TextUtil.translate("&4B"));
            banPortion.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban " + sender.getName()));
            message.addExtra(banPortion);

            TextComponent goPortion = new TextComponent(TextUtil.translate("&5G"));
            goPortion.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/goto " + sender.getName()));
            message.addExtra(goPortion);

            TextComponent chatMessage = new TextComponent(TextUtil.translate("&8] &f") + originalMessage);
            message.addExtra(chatMessage);

            sendTo.getPlayer().spigot().sendMessage(message);
            return;
        }

        originalMessage = originalMessage.replace("%player%", translate(sender.getDisplayName()));
        sendTo.sendMessage(originalMessage);
    }

}
