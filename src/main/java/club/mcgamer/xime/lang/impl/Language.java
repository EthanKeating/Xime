package club.mcgamer.xime.lang.impl;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor @Getter @Setter
public class Language {

    private final String abbreviation;

    private String motd;
    private String version;
    private String bossBarText;
    private String serverIp;

}
