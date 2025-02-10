package club.mcgamer.xime.menu.hub;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.hub.HubServerable;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.server.Serverable;
import club.mcgamer.xime.sg.state.GameState;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.privacy.PrivacyMode;
import club.mcgamer.xime.staff.StaffServerable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MakerMenu extends FastInv {

    public MakerMenu(Profile profile) {
        super((int) (9 + (profile.getPlugin().getServerHandler().getByClass(SGMakerServerable.class).stream().anyMatch(serverable -> ((SGMakerServerable) serverable).getPrivacyMode() == PrivacyMode.PUBLIC)
                ? 9 : 0)
                + (9 * (profile.getPlugin().getServerHandler().getByClass(SGMakerServerable.class).stream()
                .filter(serverable -> ((SGMakerServerable) serverable).getPrivacyMode() == PrivacyMode.PUBLIC)
                .count() / 9))), "Custom Servers");

        ItemStack serverItem = new ItemBuilder(Material.DIAMOND)
                .amount(1)
                .name("&bMCSG Maker Game (Premium)")
                .lore("&6Start a game with your daily free total.",
                        "&eDaily remaining today: &a" + (profile.getPlayer().hasPermission("xime.iron") ? "Unlimited" : "0"),
                        "&eGames remaining this hour: &a" + (profile.getPlayer().hasPermission("xime.iron") ? "Unlimited" : "0"))
                .build();

        setItem(4, serverItem, e -> {
            ((Player) e.getWhoClicked()).performCommand("maker");
            e.setCancelled(true);
        });

        List<SGMakerServerable> makerServerables = plugin.getServerHandler().getByClass(SGMakerServerable.class).stream()
                .map(serverable -> (SGMakerServerable) serverable)
                .filter(serverable -> serverable.getPrivacyMode() == PrivacyMode.PUBLIC)
                .sorted(Comparator.comparingInt(serverable -> -serverable.getPlayerList().size()))
                .toList();

        for (int i = 0; i < makerServerables.size(); i++) {
            int index = 9 + i;

            SGMakerServerable serverable = makerServerables.get(i);
            GameState gameState = serverable.getGameState();
            String serverName = "&a" + serverable.getOwner().getDisplayNameBypassDisguise() + "&e's Game";

            serverItem = switch (gameState) {
                case LOBBY -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(5)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&aLOBBY")
                        .build();
                case PREGAME -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(4)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&ePREGAME")
                        .build();
                case LIVEGAME -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(4)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&eLIVEGAME")
                        .build();
                case PREDEATHMATCH -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(14)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&cPREDEATHMATCH")
                        .build();
                case DEATHMATCH -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(14)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&cDEATHMATCH")
                        .build();
                case ENDGAME -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(14)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&cENDGAME")
                        .build();
                case CLEANUP -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(14)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&cCLEANUP")
                        .build();
                case RESTARTING -> new ItemBuilder(Material.STAINED_CLAY)
                        .data(15)
                        .amount(Math.max(1, serverable.getPlayerList().size()))
                        .name(serverName)
                        .lore(String.format("&cPlayers: &f%s/%s", serverable.getPlayerList().size(), serverable.getMaxPlayers()), "", "&8RESTARTING")
                        .build();
                default -> serverItem;
            };

            setItem(index, serverItem, e -> {
                profile.getPlayer().performCommand("join sgmaker " + serverable.getServerId());
                e.setCancelled(true);
            });
        }

    }


}
