package club.mcgamer.xime.map;

import club.mcgamer.xime.profile.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;

@Getter
@RequiredArgsConstructor
public class VoteableMap {

    private final String mapIdentifier;
    private final MapData mapData;
    private final int mapIndex;
    private HashSet<Profile> votedFor = new HashSet<>();

    public int getVotes() {
        return votedFor.stream()
                .mapToInt(profile -> profile.getPlayer().hasPermission("hq.media") ? 3
                        : profile.getPlayer().hasPermission("hq.donator") ? 2
                        : 1)
                .sum();
    }

}
