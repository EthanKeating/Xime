package club.mcgamer.xime.sgmaker.config.impl;

public enum TeamType {

    NO_TEAMS,
    TEAM_VS_TEAM,
    TEAMS_OF_2,
    TEAMS_OF_3,
    TEAMS_OF_4,
    TEAMS_OF_6;

    public String getName() {
        switch (this) {
            case TEAM_VS_TEAM:
                return "Team vs Team";
            case TEAMS_OF_2:
                return "Teams of 2";
            case TEAMS_OF_3:
                return "Teams of 3";
            case TEAMS_OF_4:
                return "Teams of 4";
            case TEAMS_OF_6:
                return "Teams of 6";
        }
        return "No Teams";
    }

}
