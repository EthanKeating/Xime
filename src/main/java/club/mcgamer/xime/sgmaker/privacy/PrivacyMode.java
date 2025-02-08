package club.mcgamer.xime.sgmaker.privacy;

import club.mcgamer.xime.hub.data.HubSpeed;

public enum PrivacyMode {

    PRIVATE,
    PUBLIC;

    public PrivacyMode getNext() {
        PrivacyMode[] enumConstants = PrivacyMode.values();
        int currentOrdinal = this.ordinal();

        if (currentOrdinal + 1 < enumConstants.length) {
            return enumConstants[currentOrdinal + 1];
        } else {
            return null;
        }
    }

}
