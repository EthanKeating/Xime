package club.mcgamer.xime.disguise;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.rank.RankHandler;
import club.mcgamer.xime.rank.impl.Rank;
import club.mcgamer.xime.util.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class DisguiseData {

    private final Profile profile;
    private final UUID uuid;
    private final String name;
    private final Skin skin;
    private Rank rank;
    private PlayerData mockData;

    public DisguiseData(Profile profile, UUID uuid, String name, Skin skin) {
        this.profile = profile;
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
        this.rank = RankHandler.DEFAULT_RANK;
    }

    public DisguiseData(Profile profile, UUID uuid, String name, Skin skin, Rank rank) {
        this.profile = profile;
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
        this.rank = rank;
    }


}
