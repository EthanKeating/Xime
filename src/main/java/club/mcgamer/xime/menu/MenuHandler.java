package club.mcgamer.xime.menu;

import club.mcgamer.xime.XimePlugin;
import club.mcgamer.xime.fastinv.FastInvManager;
import lombok.Getter;

@Getter
public class MenuHandler {

    public MenuHandler(XimePlugin plugin) {
        FastInvManager.register(plugin);
    }


}
