package club.mcgamer.xime.lang;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.lang.impl.Language;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

@Getter
public class LanguageHandler {

    private final XimePlugin plugin;

    private final HashMap<String, Language> languages = new HashMap<>();

    private final Language defaultLanguage = new Language("en");

    public LanguageHandler(XimePlugin plugin) {
        this.plugin = plugin;

        languages.put(defaultLanguage.getAbbreviation(), defaultLanguage);
        load();
    }

    public Language getLanguage(String abbreviation) {
        return languages.getOrDefault(abbreviation, defaultLanguage);
    }

    public void load() {
        for(Language language : languages.values())
            populateLanguage(language);
    }

    private void populateLanguage(Language language) {
        //get language config file from language.getAbbreviation()
        FileConfiguration languageConfiguration = plugin.getConfig();

        language.setMotd(languageConfiguration.getString("server.motd"));
        language.setVersion(languageConfiguration.getString("server.version"));
        language.setServerIp(languageConfiguration.getString("server.ip"));
        language.setTabHeader(languageConfiguration.getString("tab.header"));
        language.setTabFooter(languageConfiguration.getString("tab.footer"));

        language.setBossBarText(languageConfiguration.getString("bossbar.text").replace("%version%", language.getVersion()));
    }

}
