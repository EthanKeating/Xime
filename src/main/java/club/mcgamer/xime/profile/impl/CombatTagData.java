package club.mcgamer.xime.profile.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class CombatTagData {

    private UUID attackedBy;
    private long attackedAt;

    public boolean isActive() {
        return System.currentTimeMillis() - attackedAt < (30 * 1000);
    }

}
