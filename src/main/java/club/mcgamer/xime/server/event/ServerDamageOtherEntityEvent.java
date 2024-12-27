package club.mcgamer.xime.server.event;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@RequiredArgsConstructor
public class ServerDamageOtherEntityEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Entity victim;
    @Getter private final Profile attacker;
    @Getter private final Serverable serverable;
    @Getter private final EntityDamageByEntityEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
