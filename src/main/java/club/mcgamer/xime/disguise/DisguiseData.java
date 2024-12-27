package club.mcgamer.xime.disguise;

import club.mcgamer.xime.util.Skin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DisguiseData {

    private final UUID uuid;
    private final String name;
    private final Skin skin;
    //private final Rank rank;
    //private final ProfileData mockProfileData;

    public DisguiseData(UUID uuid, String name, Skin skin) {
        this.uuid = uuid;
        this.name = name;
        this.skin = skin;
        //this.rank = rank;

        //mockProfileData = ProfileData.createMock(name, rank == null ? "None" : rank.getName());
    }


}
