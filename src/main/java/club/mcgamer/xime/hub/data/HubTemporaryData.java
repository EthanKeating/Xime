package club.mcgamer.xime.hub.data;

import club.mcgamer.xime.server.data.TemporaryData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HubTemporaryData extends TemporaryData {

    private boolean hidePlayers = false;
    private HubSpeed hubSpeed = HubSpeed.NORMAL;

}