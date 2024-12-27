package club.mcgamer.xime.sg.state;

public enum GameState {

    LOBBY,
    LOADING,
    PREGAME,
    LIVEGAME,
    PREDEATHMATCH,
    DEATHMATCH,
    CLEANUP,
    RESTARTING;

    public String getName() {
        switch (this) {
            case LOBBY:
                return "Lobby";
            case LOADING:
                return "Loading";
            case PREGAME:
                return "PreGame";
            case LIVEGAME:
                return "LiveGame";
            case PREDEATHMATCH:
                return "PreDeathmatch";
            case DEATHMATCH:
                return "Deathmatch";
            case CLEANUP:
                return "Cleanup";
            case RESTARTING:
                return "Restarting";
        }
        return "";
    }

    public GameState getNext() {
        GameState[] enumConstants = GameState.values();
        int currentOrdinal = this.ordinal();

        if (currentOrdinal + 1 < enumConstants.length) {
            return enumConstants[currentOrdinal + 1];
        } else {
            return null;
        }
    }

}
