package club.mcgamer.xime.bg.data;

import club.mcgamer.xime.server.data.TemporaryData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BGTemporaryData extends TemporaryData {

    private boolean waiting = true;
    private int kills;

}
