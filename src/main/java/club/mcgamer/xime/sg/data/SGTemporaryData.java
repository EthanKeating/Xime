package club.mcgamer.xime.sg.data;

import club.mcgamer.xime.server.data.TemporaryData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter @Setter
public class SGTemporaryData extends TemporaryData {

    private int districtId;
    private Location pedistalLocation;

    private int killCount = 0;
    private int bounty = 0;

    private int previousSpectateIndex = 0;

}
