package club.mcgamer.xime.loot;

public enum LootStyle {

    DEFAULT,
    ONLY_TIER_1,
    ONLY_TIER_2,
    INVERTED,
    MIXED_CHESTS,
    MIXED_ITEMS;

    public String getName() {
        switch (this) {
            case ONLY_TIER_1:
                return "Only Tier 1s";
            case ONLY_TIER_2:
                return "Only Tier 2s";
            case INVERTED:
                return "Inverted";
            case MIXED_CHESTS:
                return "Mixed Chests";
            case MIXED_ITEMS:
                return "Mixed Items";
        }
        return "Default";
    }

}
