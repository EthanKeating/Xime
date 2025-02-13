package club.mcgamer.xime.sgmaker.config.type;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.CWLootTable;
import club.mcgamer.xime.loot.tables.MCSGLootTable;
import club.mcgamer.xime.sgmaker.config.MakerConfig;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CWConfig extends MakerConfig {

    private String name = "Clan War Config";

    private int minimumPlayers = 2;
    private int maximumPlayers = 24;
    private int deathmatchPlayers = 0;

    private int lobbyLength = (3 * 60); // 3 minutes
    private int preGameLength = 15;
    private int liveGameLength = 10; //30 minutes
    private int preDeathmatchTime = 0;
    private int deathmatchTime = 3; //3 minutes
    private int endGameTime = 10;

    private int gracePeriodTime = 60;
    private int deathmatchShrinkTime = 2 * 60; //2 minutes

    private double rodSpeedMultiplier = 1.0;
    private double maxHealth = 20.0;
    private boolean displayHealth = true;

    private boolean silentJoinLeave = false;
    private boolean randomizeNames = false;
    private boolean itemizedFlowers = false;
    private boolean dayLightCycle = false;
    private boolean naturalRegeneration = true;
    private boolean noHitDelay = false;

    private LootTable lootTable = new CWLootTable();
    private LootStyle lootStyle = LootStyle.MIXED_ITEMS;
}
