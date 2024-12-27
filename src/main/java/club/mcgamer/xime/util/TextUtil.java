package club.mcgamer.xime.util;

import lombok.experimental.UtilityClass;
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

    public String toPascalCase(String string) {
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

}
