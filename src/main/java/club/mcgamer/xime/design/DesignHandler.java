package club.mcgamer.xime.design;

import club.mcgamer.xime.XimePlugin;

public class DesignHandler {

    public DesignHandler(XimePlugin plugin) {
        new DesignThread(plugin).start();
    }


}
