package club.mcgamer.xime.sgmaker.config.template;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.MCSGLootTable;
import club.mcgamer.xime.sgmaker.config.MakerConfig;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CWMakerConfig extends MakerConfig {

    private LootTable lootTable = new MCSGLootTable();
    private LootStyle lootStyle = LootStyle.MIXED_ITEMS;

    private TeamType teamType = TeamType.TEAM_VS_TEAM;
    private boolean randomTeams = false;

    private int preGameLength = 10;
    private int liveGameLength = 10 * 60;

}
