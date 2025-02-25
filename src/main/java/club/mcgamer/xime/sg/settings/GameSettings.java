package club.mcgamer.xime.sg.settings;

import club.mcgamer.xime.loot.LootStyle;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.MCSGLootTable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.data.SGTeamProvider;
import club.mcgamer.xime.sgmaker.config.MakerConfig;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class GameSettings {


    private final SGServerable serverable;
    protected SGTeamProvider teamProvider;

    private int minimumPlayers = 5;
    private int maximumPlayers = 24;
    private int deathmatchPlayers = 3;

    private int lobbyLength = (2 * 60); // 2 minutes
    private int preGameLength = 30;
    private int liveGameLength = 30 * 60; //30 minutes
    private int preDeathmatchTime = 10;
    private int deathmatchTime = 3 * 60; //3 minutes
    private int endGameTime = 10;

    private int gracePeriodTime = -1;
    private int deathmatchShrinkTime = 2 * 60; //2 minutes

    private double rodSpeedMultiplier = 1.0;
    private double maxHealth = 20.0;
    private boolean displayHealth = false;

    private boolean silentJoinLeave = false;
    private boolean randomizeNames = false;
    private boolean itemizedFlowers = false;
    private boolean dayLightCycle = true;
    private boolean naturalRegeneration = true;
    private boolean noHitDelay = false;

    private LootTable lootTable = new MCSGLootTable();
    private LootStyle lootStyle = LootStyle.DEFAULT;

    public GameSettings(SGServerable serverable) {
        this.serverable = serverable;
        this.teamProvider = new SGTeamProvider(serverable);
    }

    public void load(MakerConfig config) {
        if (this.deathmatchPlayers != config.getDeathmatchPlayers()) {
            serverable.announce("");
            this.deathmatchPlayers = config.getDeathmatchPlayers();
        }
    }


}
