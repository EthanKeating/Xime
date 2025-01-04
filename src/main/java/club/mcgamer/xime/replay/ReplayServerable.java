package club.mcgamer.xime.replay;

import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;

public class ReplayServerable extends Serverable {

    @Override
    public TemporaryData createTemporaryData() {
        return TemporaryData.DEFAULT;
    }
}
