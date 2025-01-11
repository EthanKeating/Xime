package club.mcgamer.xime.sgmaker.config;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MakerConfig {
    private LootTable lootTable;
    private LootStyle lootStyle;

    private TeamType teamType;
    private boolean randomTeams;

    private int gameLength;

}
