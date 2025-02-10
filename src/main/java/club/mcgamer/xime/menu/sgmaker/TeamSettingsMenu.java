package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TeamSettingsMenu extends FastInv {

    public static ItemStack TEAM_ITEM = new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&bTeam Selector").build();

    public TeamSettingsMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(18, TextUtil.translate("Team Settings"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        for(int index = 0; index < TeamType.values().length; index++) {
            TeamType type = TeamType.values()[index];

            setItem(index + 2, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + type.getName())
                            .build(),
                    e -> {
                        serverable.getGameSettings().getTeamProvider().setTeamType(type);
                        serverable.announce("&6Team Mode: &e" + type.getName());
                        previousMenu.open(profile.getPlayer());

                        if (type == TeamType.NO_TEAMS) {
                            serverable.getPlayerList().forEach(profile1 -> {
                                profile1.getPlayer().getInventory().setItem(0, new ItemStack(Material.AIR));
                                profile1.getPlayer().closeInventory();
                            });
                        } else {
                            serverable.getPlayerList().forEach(profile1 -> profile1.getPlayer().getInventory().setItem(0, TEAM_ITEM));
                        }
                    });

            setItem(13, new ItemBuilder(Material.IRON_SWORD)
                            .name("&bFriendly Fire")
                            .build(),
                    e -> {
                        new TeamDamageSubMenu(this, profile, serverable).open(profile.getPlayer());
                    });
        }
    }
}

