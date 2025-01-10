package club.mcgamer.xime.bg;

import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.server.data.TemporaryData;

public class BGServerable extends Serverable {

    public BGServerable() {
        super();

        setMaxPlayers(100);
    }

    @Override
    public TemporaryData createTemporaryData() {
        return TemporaryData.DEFAULT;
    }

}
