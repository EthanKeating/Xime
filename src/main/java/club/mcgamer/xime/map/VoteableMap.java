package club.mcgamer.xime.map;

import club.mcgamer.xime.profile.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashSet;

@Getter
@RequiredArgsConstructor
public class VoteableMap {

    private final String mapIdentifier;
    private final MapData mapData;
    private final int mapIndex;
    private HashSet<Profile> votedFor = new HashSet<>();

    public int getVotes() {
        int total = 0;

        for(Profile profile : votedFor) {
            Player player = profile.getPlayer();

            if (player.hasPermission("xime.quantum"))
                total += 6;
            else if (player.hasPermission("xime.platinum"))
                total += 5;
            else if (player.hasPermission("xime.diamond"))
                total += 4;
            else if (player.hasPermission("xime.gold"))
                total += 3;
            else if (player.hasPermission("xime.iron"))
                total += 2;
            else
                total += 1;
        }

        return total;
    }

}
