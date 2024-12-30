package club.mcgamer.xime.sg.settings;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.MCSGLootTable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class GameSettings {

    private int minimumPlayers = 6;
    private int maximumPlayers = 24;
    private int deathmatchPlayers = 3;

    private int lobbyLength = 3 * 60; // 3 minutes
    private int preGameLength = 30;
    private int liveGameLength = 30 * 60; //30 minutes
    private int preDeathmatchTime = 10;
    private int deathmatchTime = 3 * 60; //3 minutes
    private int cleanupTime = 10;

    private int deathmatchShrinkTime = 2 * 60; //2 minutes

    private boolean silentJoinLeave = false;

    private LootTable lootTable = new MCSGLootTable();
    private LootStyle lootStyle = LootStyle.DEFAULT;


}
