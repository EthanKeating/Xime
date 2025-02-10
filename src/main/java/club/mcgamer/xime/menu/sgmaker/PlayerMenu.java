package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerMenu extends FastInv {

    public PlayerMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable, int page) {
        super(18 + (9 * ((serverable.getPlayerList().size() - 1) / 9)), TextUtil.translate(String.format("Manage a player", profile.getPlugin().getMapHandler().getMapPool().size(), page)));

        for (int i = 1; i < 9; i++) {
            setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .data(15)
                            .name("&7")
                            .build(),
                    e -> {
                        e.setCancelled(true);
                    });
        }
        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(14)
                        .name("&aBack")
                        .build(),
                e -> {
                    e.setCancelled(true);
                    previousMenu.open(profile.getPlayer());
                });

        List<Profile> players = serverable.getPlayerList().stream().filter(loopProfile -> loopProfile != serverable.getOwner()).toList();
        for (int i = 0; i < players.size(); i++) {
            int index = 9 + i;
            Profile loopProfile = players.get(i);

            setItem(index, new ItemBuilder(Material.SKULL_ITEM)
                            .data(3)
                            .owner(loopProfile.getPlayer())
                            .name(loopProfile.getDisplayName())
                            .lore("&eClick to kick " + loopProfile.getDisplayName()).build(),
                    e -> {
                        e.setCancelled(true);
                        loopProfile.sendMessage(loopProfile.getServerable().getPrefix() + "&cYou have been removed from the game by the host&8.");
                        plugin.getServerHandler().getFallback().add(loopProfile);
                        serverable.getInvitedPlayers().remove(loopProfile.getUuid());
                        new PlayerMenu(previousMenu, profile, serverable, page).open(profile.getPlayer());
                    });
        }
    }
}

