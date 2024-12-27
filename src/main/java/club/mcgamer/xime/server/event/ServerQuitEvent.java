package club.mcgamer.xime.server.event;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//Make wrappers for every single event that I use
@Getter @RequiredArgsConstructor
public class ServerQuitEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Profile profile;
    private final Serverable serverable;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
