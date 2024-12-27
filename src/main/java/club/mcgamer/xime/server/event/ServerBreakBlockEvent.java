package club.mcgamer.xime.server.event;

import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

@RequiredArgsConstructor
public class ServerBreakBlockEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Profile profile;
    @Getter private final Serverable serverable;
    @Getter private final Block block;
    @Getter private final BlockBreakEvent event;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
