package club.mcgamer.xime.sg.data;

import club.mcgamer.xime.server.data.TemporaryData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter @Setter
public class SGTemporaryData extends TemporaryData {

    private int districtId;
    private Location pedistalLocation;

    private int killCount = 0;
    private int chestCount = 0;
    private int bounty = 0;

    private int previousSpectateIndex = 0;
    private long lastSpectate = System.currentTimeMillis();

    private long lifeStart;

    private UUID attackedBy;
    private long attackedAt;

    public boolean isActive() {
        return System.currentTimeMillis() - attackedAt < (30 * 1000);
    }

    public boolean canSpectate() { return System.currentTimeMillis() - lastSpectate > 500; }

}
