package club.mcgamer.xime.profile.data.impl;

public enum SidebarType {

    DEFAULT,
    MINIMIZE,
    TWENTY_FOURTEEN,
    TWENTY_FIFTEEN;

    public String getName() {
        switch (this) {
            case MINIMIZE:
                return "Minimize";
            case TWENTY_FOURTEEN:
                return "2014";
            case TWENTY_FIFTEEN:
                return "2015";
        }
        return "Default";
    }

}
