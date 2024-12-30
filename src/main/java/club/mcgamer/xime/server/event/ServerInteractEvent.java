package club.mcgamer.xime.server.event;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ServerInteractEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Profile profile;
    @Getter private final Serverable serverable;
    @Getter private final ItemStack itemStack;
    @Getter private final PlayerInteractEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
