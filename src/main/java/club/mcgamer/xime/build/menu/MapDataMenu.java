package club.mcgamer.xime.build.menu;

import club.mcgamer.xime.build.BuildServerable;
import club.mcgamer.xime.build.input.InputType;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class MapDataMenu extends FastInv {

    public MapDataMenu(Profile profile, BuildServerable serverable) {
        super(9, TextUtil.translate("Map Data Menu"));

        setItem(1, new ItemBuilder(Material.ANVIL)
                        .amount(1)
                        .name("&aSet Map Name")
                        .lore("&7You will be prompted to type the Map Name into chat.")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();

                    serverable.setInputType(InputType.NAME);
                    profile.sendMessage("&a")
                            .sendMessage("&a&lPlease enter the map name:")
                            .sendMessage("&a");
                });
        setItem(4, new ItemBuilder(Material.SKULL_ITEM)
                        .data(3)
                        .amount(1)
                        .name("&aSet Map Author(s)")
                        .lore("&7You will be prompted to type the Map Author(s) into chat.")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();

                    serverable.setInputType(InputType.AUTHOR);
                    profile.sendMessage("&a")
                            .sendMessage("&a&lPlease enter the map author(s):")
                            .sendMessage("&a");
                });
        setItem(7, new ItemBuilder(Material.EYE_OF_ENDER)
                        .amount(1)
                        .name("&aSet Map Link")
                        .lore("&7You will be prompted to type the Map Link into chat.")
                        .build(),
                e -> {
                    profile.getPlayer().closeInventory();

                    serverable.setInputType(InputType.LINK);
                    profile.sendMessage("&a")
                            .sendMessage("&a&lPlease enter the map link:")
                            .sendMessage("&a");
                });
    }
}

