package club.mcgamer.xime.sg.data;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;

@RequiredArgsConstructor
public class SGTeam {

    private final SGServerable serverable;
    @Getter private final int teamId;
    @Getter private final HashSet<Profile> originalPlayers = new HashSet<>();
    @Getter private final HashSet<Profile> players = new HashSet<>();
    @Getter private final int maxPlayers;

    public boolean addPlayer(Profile profile) {
        if (players.size() >= maxPlayers)
            return false;

        players.add(profile);
        return true;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean removePlayer(Profile profile) {
        if (!players.contains(profile))
            return false;

        players.remove(profile);
        return true;
    }

    public void setOriginalPlayers() {
        originalPlayers.clear();
        originalPlayers.addAll(players);
    }

    public HashSet<Profile> getEveryPlayer() {
        HashSet<Profile> everyPlayer = new HashSet<>();

        everyPlayer.addAll(originalPlayers);
        everyPlayer.addAll(players);

        return everyPlayer;
    }

}
