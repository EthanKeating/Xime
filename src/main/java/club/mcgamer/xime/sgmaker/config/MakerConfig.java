package club.mcgamer.xime.sgmaker.config;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.MCSGLootTable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MakerConfig {

    private LootTable lootTable = new MCSGLootTable();
    private LootStyle lootStyle = LootStyle.DEFAULT;

    private TeamType teamType = TeamType.NO_TEAMS;
    private boolean randomTeams;

    private int gameLength;

}
