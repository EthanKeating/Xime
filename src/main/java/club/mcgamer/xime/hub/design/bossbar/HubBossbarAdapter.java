package club.mcgamer.xime.hub.design.bossbar;

import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;

public class HubBossbarAdapter extends BossbarAdapter {

    private float health = 300.0f;

    @Override
    public void tick() {
        //health -= 1.0f;

        if (health < 0.0)
            health = 300.0f;

    }

    @Override
    public String getTitle(Profile profile) {
        return String.format("&eMCGamer Network &6Beta &7(%s)&8. &aFeatures/gameplay subject to change.", ServerHandler.SERVER_VERSION);
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
