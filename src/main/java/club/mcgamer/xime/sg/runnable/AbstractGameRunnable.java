package club.mcgamer.xime.sg.runnable;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractGameRunnable extends BukkitRunnable {

    public void forceCancel() {
        super.cancel();
    }

    public abstract void run();
}
