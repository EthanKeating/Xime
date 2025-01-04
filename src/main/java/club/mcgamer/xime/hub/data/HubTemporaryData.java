package club.mcgamer.xime.hub.data;

import club.mcgamer.xime.server.data.TemporaryData;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter @Setter
public class HubTemporaryData extends TemporaryData {

    private boolean hidePlayers = false;
    private HubSpeed hubSpeed = HubSpeed.NORMAL;

}