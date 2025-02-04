package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.staff.StaffServerable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HubMainMenu extends FastInv {

    public HubMainMenu(Profile profile) {
        super(45, "MCGamer Network Navigation");

        int sgPlayers = plugin.getServerHandler().getByClass(SGServerable.class).stream()
                .mapToInt(serverable -> serverable.getPlayerList().size())
                .sum();

        int bgPlayers = plugin.getServerHandler().getByClass(BGServerable.class).stream()
                .mapToInt(serverable -> serverable.getPlayerList().size())
                .sum();

        int hubPlayers = plugin.getServerHandler().getByClass(HubServerable.class).stream()
                .mapToInt(serverable -> serverable.getPlayerList().size())
                .sum();

        setItem(10, new ItemBuilder(Material.BOW).name("&6            Survival Games")
                .lore(Arrays.asList(
                        "&8████████████████",
                        "",
                        String.format("&b       [ Join &e%s &bPlayer%s ]", sgPlayers, sgPlayers == 1 ? "" : "s"),
                        "",
                        "&e The premier MCSG© experience!",
                        "&e    Join 23 other tributes in",
                        "&e      a fight for victory!",
                        "",
                        "&b Left Click &7to &b&lBrowse Servers",
                        "    &bRight Click &7to &b&lPlay Now",
                        "",
                        "&8████████████████"
                ))
                .build(), e -> {

            e.setCancelled(true);
            if (profile.getServerable() instanceof StaffServerable) {
                new SGSubMenu(profile).open(profile.getPlayer());
                return;
            }

            switch (e.getClick()) {
                case LEFT:
                case SHIFT_LEFT:
                    new SGSubMenu(profile).open(profile.getPlayer());
                    return;
                case RIGHT:
                case SHIFT_RIGHT:
                    profile.getPlayer().performCommand("join sg");
            }
        });


        setItem(16, new ItemBuilder(Material.IRON_SWORD).name("&6            Battlegrounds")
                .lore(Arrays.asList(
                        "&8████████████████",
                        "",
                        String.format("&b       [ Join &e%s &bPlayer%s ]", bgPlayers, bgPlayers == 1 ? "" : "s"),
                        "",
                        "     &eA PvP free-for-all, fight",
                        "           &eyour way to the",
                        "       &etop in Battlegrounds!",
                        "",
                        "&b Left Click &7to &b&lBrowse Servers",
                        "    &bRight Click &7to &b&lPlay Now",
                        "",
                        "&8████████████████"
                ))
                .build(), e -> {

            e.setCancelled(true);
            switch (e.getClick()) {
                case LEFT:
                case SHIFT_LEFT:
                    new BGSubMenu(profile).open(profile.getPlayer());
                    return;
                case RIGHT:
                case SHIFT_RIGHT:
                    profile.getPlayer().performCommand("join bg");
            }
        });

        setItem(22, new ItemBuilder(Material.WATCH).name("             &2MCGamer Hub")
                .lore(Arrays.asList(
                        "&8████████████████",
                        "",
                        String.format("&b       [ Join &e%s &bPlayer%s ]", hubPlayers, hubPlayers == 1 ? "" : "s"),
                        "",
                        "&aUse the MCGamer hub to travel",
                        "&a  between the different games.",
                        "&a    Take a moment to hang out",
                        "&a     and chat with friends, or",
                        "&a          join a game and",
                        "&a     get right into the action!",
                        "",
                        "&b Left Click &7to &b&lBrowse Servers",
                        "",
                        "&8████████████████")).build(), e -> {

            e.setCancelled(true);
            switch (e.getClick()) {
                case LEFT:
                case SHIFT_LEFT:
                    new HubSelectorMenu(profile).open(profile.getPlayer());
            }
        });

        setItem(28, new ItemBuilder(Material.CHEST).name("&2  MCGamer Maker / MCSG Maker")
                .lore(Arrays.asList(
                        "&8████████████████",
                        "",
                        "&aCreate your own, private games",
                        "&a  with friends, with a range of ",
                        "&a customizable gameplay options!",
                        "",
                        "&bLeft Click &7to &b&lMaker Menu",
                        "",
                        "&8████████████████"
                ))
                .build(), e -> {

            e.setCancelled(true);

            switch (e.getClick()) {
                case LEFT:
                case SHIFT_LEFT:
                    new MakerMenu(profile).open(profile.getPlayer());
            }
        });

        setItem(34, new ItemBuilder(Material.IRON_FENCE).name("&c     In Development")
                .lore(Arrays.asList(
                        "&8████████████████",
                        "",
                        "&cThis gamemode is currently in",
                        "&cdevelopment, come back another",
                        "&ctime to check its availability",
                        "",
                        "&8████████████████"
                ))
                .build(), e -> {

            e.setCancelled(true);
        });
    }
}
