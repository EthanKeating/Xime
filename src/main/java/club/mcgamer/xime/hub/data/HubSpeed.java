package club.mcgamer.xime.hub.data;

import club.mcgamer.xime.util.TextUtil;

public enum HubSpeed {
    NORMAL,
    SPEEDY,
    SUPER_SPEEDY;

    public String getName() {
        return this.name().toLowerCase().replace('_', ' ');
    }

    public HubSpeed getNext() {
        HubSpeed[] enumConstants = HubSpeed.values();
        int currentOrdinal = this.ordinal();

        if (currentOrdinal + 1 < enumConstants.length) {
            return enumConstants[currentOrdinal + 1];
        } else {
            return null;
        }
    }
}
