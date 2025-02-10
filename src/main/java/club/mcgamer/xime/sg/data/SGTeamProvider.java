package club.mcgamer.xime.sg.data;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.config.TeamDamageType;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;

@RequiredArgsConstructor
public class SGTeamProvider {

    private final SGServerable serverable;
    @Getter private TeamType teamType = TeamType.NO_TEAMS;
    @Getter @Setter private TeamDamageType teamDamageType = TeamDamageType.PROJECTILE_ONLY;

    private HashMap<Integer, SGTeam> teams = new HashMap<>();

    int teamCount = 0;
    int teamPlayers = 0;

    public void removeEmpty() {
        for(int teamId : new ArrayList<>(teams.keySet())) {
            SGTeam team = teams.get(teamId);
            if (team.getPlayers().isEmpty())
                teams.remove(teamId);
        }
    }

    public void addPlayer(Profile profile, int teamId) {
        SGTeam oldTeam = getTeam(profile);
        if (oldTeam != null)
            oldTeam.removePlayer(profile);

        SGTeam newTeam = teams.get(teamId);
        if (newTeam != null) {
            if (newTeam == oldTeam) {
                profile.sendMessage(serverable.getPrefix() + "&cYou have left &6Team #" + newTeam.getTeamId());
                newTeam.removePlayer(profile);
                return;
            }

            newTeam.addPlayer(profile);
            profile.sendMessage(serverable.getPrefix() + "&aYou have joined &6Team #" + newTeam.getTeamId());
        }


    }

    public Collection<SGTeam> getTeams() {
        return new ArrayList<>(teams.values());
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;

        teams.clear();

        int maximumPlayers = serverable.getGameSettings().getMaximumPlayers();

        switch (teamType) {
            case TEAMS_OF_2:
                //So 25 -> 13 teams not 12 (cast 12.5 -> 12)
                teamCount = (maximumPlayers+1) / 2;
                teamPlayers = 2;
                break;
            case TEAMS_OF_3:
                teamCount = (maximumPlayers+1) / 3;
                teamPlayers = 3;
                break;
            case TEAMS_OF_4:
                teamCount = (maximumPlayers+1) / 4;
                teamPlayers = 4;
                break;
            case TEAMS_OF_6:
                teamCount = (maximumPlayers+1) / 6;
                teamPlayers = 6;
                break;
            case TEAM_VS_TEAM:
                teamCount = 2;
                teamPlayers = (maximumPlayers+1) / 2;
                break;
        }

        teams.clear();
        for(int i = 1; i <= teamCount; i++) {
            teams.put(i, new SGTeam(serverable, i, teamPlayers));
        }
    }

    public boolean hasTeam(Profile profile) {
        for (SGTeam team : teams.values())
            if (team.getPlayers().contains(profile))
                return true;

        return false;
    }

    public boolean hasTeamOriginal(Profile profile) {
        for (SGTeam team : teams.values())
            if (team.getOriginalPlayers().contains(profile))
                return true;

        return false;
    }

    public SGTeam getTeam(Profile profile) {
        for (SGTeam team : teams.values())
            if (team.getPlayers().contains(profile))
                return team;

        return null;
    }

    public SGTeam getTeamOriginal(Profile profile) {
        SGTeam thisTeam = getTeam(profile);
        if (thisTeam != null)
            return thisTeam;

        for (SGTeam team : teams.values())
            if (team.getOriginalPlayers().contains(profile))
                return team;

        return null;
    }

    public SGTeam getTeam(int teamId) {
        return teams.get(teamId);
    }

}
