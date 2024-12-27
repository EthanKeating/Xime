package club.mcgamer.xime.server.event;

import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ServerStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Serverable serverable;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    public ServerStartEvent(Serverable serverable) {
        this.serverable = serverable;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
