package club.mcgamer.xime.hub.design.bossbar;

import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.profile.Profile;

public class HubBossbarAdapter extends BossbarAdapter {

    private float health = 300.0f;

    @Override
    public void tick() {
        health -= 1.0f;

        if (health < 0.0)
            health = 300.0f;

    }

    @Override
    public String getTitle(Profile profile) {
        return "&eMCGamer Network &6Alpha &7[5.0.0] &aFeatures/gameplay subject to change.";
    }

    @Override
    public float getHealth(Profile profile) {
        return health;
    }

    @Override
    public boolean isHidden(Profile profile) {
        return false;
    }
}
