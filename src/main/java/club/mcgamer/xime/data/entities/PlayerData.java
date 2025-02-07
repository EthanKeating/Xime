package club.mcgamer.xime.data.entities;

import club.mcgamer.xime.profile.Profile;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@Getter @Setter @NoArgsConstructor
@DatabaseTable(tableName = "playerdata")
public class PlayerData {

    private static final Random random = new Random();

    @DatabaseField(id = true)
    private String uuid;

    @DatabaseField(canBeNull = false, defaultValue = "Regular")
    private String userRank = "Regular";

    @DatabaseField(canBeNull = false, defaultValue = "")
    private String userName = "";

    @DatabaseField(canBeNull = false, defaultValue = "")
    private String displayName = "";

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private long firstJoin = System.currentTimeMillis();

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private long lastSeen = System.currentTimeMillis();

    @DatabaseField(canBeNull = false, defaultValue = "false")
    private boolean silentJoin = false;

    @DatabaseField(canBeNull = false, defaultValue = "false")
    private boolean canFly = false;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sidebarType = 0;

    @DatabaseField(canBeNull = false, defaultValue = "1000")
    private int sgPoints = 1000;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgGamesWon = 0;

    @DatabaseField(canBeNull = false, defaultValue = "-1")
    private int sgGameRank = -1;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgGamesPlayed = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgDeathmatches = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgKills = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgMostKills = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgDeaths = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgChests = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgMostChests = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private long sgLifeSpan = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private long sgLongestLifeSpan = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgBountiesSent = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int sgBountiesReceived = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int bgSwordSlot = 0;

    @DatabaseField(canBeNull = false, defaultValue = "1")
    private int bgRodSlot = 1;

    @DatabaseField(canBeNull = false, defaultValue = "2")
    private int bgBowSlot = 2;

    @DatabaseField(canBeNull = false, defaultValue = "3")
    private int bgFNSSlot = 3;

    @DatabaseField(canBeNull = false, defaultValue = "4")
    private int bgGapSlot = 4;

    @DatabaseField(canBeNull = false, defaultValue = "8")
    private int bgArrowSlot = 8;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int bgKills = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int bgDeaths = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int bgWins = 0;

    @DatabaseField(canBeNull = false, defaultValue = "0")
    private int bgBowKills = 0;

    public static PlayerData createMock(Profile profile) {
        PlayerData profileData = new PlayerData();

        profileData.uuid = profile.getUuid().toString();
        profileData.userRank = profile.getRank().getName();
        profileData.displayName = profile.getDisplayName();

        int baseGameCount = 20 + random.nextInt(20);
        profileData.sgPoints = 100 + random.nextInt(500);
        profileData.sgLifeSpan = ((baseGameCount * 15) * 60) * 1000;
        profileData.sgLongestLifeSpan = ((baseGameCount * 15 + (random.nextInt(10)) * 60) + random.nextInt(60)) * 1000;

        profileData.sgGamesPlayed = baseGameCount;
        profileData.sgGamesWon = baseGameCount / (baseGameCount / 2) + random.nextInt(baseGameCount / (baseGameCount / 2));
        profileData.sgDeathmatches = baseGameCount / 8 + profileData.sgGamesWon;

        profileData.sgGameRank = 100 + random.nextInt(200);
        profileData.sgMostKills = 6 + random.nextInt(10);
        profileData.sgKills = (random.nextInt(3) + 3) * (baseGameCount / 4);
        profileData.sgDeaths = baseGameCount - profileData.sgGamesWon;

        profileData.sgChests = ((random.nextInt(3) + 4) * baseGameCount) + ((random.nextInt(2) + 1) * baseGameCount);
        profileData.sgMostChests = random.nextInt(20) + 40;

        profileData.bgKills = (random.nextInt(3) + 3) * (baseGameCount / 4);
        profileData.bgDeaths = (int) (profileData.bgKills / 1.5 + random.nextInt(profileData.bgKills / 2));
        profileData.bgWins = random.nextInt(5);
        profileData.bgBowKills = profileData.bgKills / 4;

        return profileData;
    }

}
