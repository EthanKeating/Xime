package club.mcgamer.xime.server.event;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class ServerDamageEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Profile victim;
    @Getter private final Optional<Profile> attacker;
    @Getter private final Serverable serverable;
    @Getter private final EntityDamageEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
