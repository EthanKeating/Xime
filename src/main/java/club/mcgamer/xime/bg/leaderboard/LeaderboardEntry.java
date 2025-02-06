package club.mcgamer.xime.bg.leaderboard;

import club.mcgamer.xime.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class LeaderboardEntry {

    private final Profile profile;
    private final String displayName;
    private final int kills;

}
