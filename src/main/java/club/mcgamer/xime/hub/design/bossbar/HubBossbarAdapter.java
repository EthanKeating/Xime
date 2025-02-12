package club.mcgamer.xime.hub.design.bossbar;

import club.mcgamer.xime.design.bossbar.BossbarAdapter;
import club.mcgamer.xime.lang.impl.Language;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.ServerHandler;

public class HubBossbarAdapter extends BossbarAdapter {

    @Override
    public void tick() {}

    @Override
    public String getTitle(Profile profile) {
        Language language = profile.getLanguage();

        return language.getBossBarText();
    }

    @Override
    public float getHealth(Profile profile) {
        return 300.0f;
    }

    @Override
    public boolean isHidden(Profile profile) {
        return false;
    }
}
