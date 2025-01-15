package club.mcgamer.xime.server.event;

import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Serverable serverable;
    private final World world;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ServerLoadEvent(Serverable serverable, World world) {
        this.serverable = serverable;
        this.world = world;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
