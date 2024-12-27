package club.mcgamer.xime.design.bossbar;

import club.mcgamer.xime.profile.Profile;
import lombok.experimental.Accessors;
@Accessors(chain = true)
public abstract class BossbarAdapter {

    public static BossbarAdapter DEFAULT = new BossbarAdapter() {
        public void tick() { }
        public String getTitle(Profile profile) { return "&7"; }
        public float getHealth(Profile profile) { return 300; }
        public boolean isHidden(Profile profile) { return true; }
    };

    public abstract void tick();

    public abstract String getTitle(Profile profile);

    public abstract float getHealth(Profile profile);

    public abstract boolean isHidden(Profile profile);


}
