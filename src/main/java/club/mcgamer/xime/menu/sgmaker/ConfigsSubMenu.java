package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.menu.sgmaker.mutators.*;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import com.mojang.authlib.GameProfile;
import org.bukkit.Material;

public class ConfigsSubMenu extends FastInv {

    public ConfigsSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Config Templates"));

        setItem(0, new ItemBuilder(Material.RED_MUSHROOM)
                        .name("&bDisplay Health")
                        .lore("&aEnabled / disable displaying health under names.")
                        .build(),
                e -> {
                    new DisplayHealthSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                    e.setCancelled(true);
                });

    }
}

