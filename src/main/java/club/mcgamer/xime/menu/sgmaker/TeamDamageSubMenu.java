package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.sgmaker.config.TeamDamageType;
import club.mcgamer.xime.sgmaker.config.impl.TeamType;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TeamDamageSubMenu extends FastInv {

    public TeamDamageSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Friendly Fire Settings"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, TeamDamageType>> damageTypes = Arrays.asList(
                new Pair<>("Disabled", TeamDamageType.NO_TEAM_DAMAGE),
                new Pair<>("Projectiles Only", TeamDamageType.PROJECTILE_ONLY),
                new Pair<>("Enabled, No Damage", TeamDamageType.ATTACK_BUT_NO_DAMAGE),
                new Pair<>("Enabled", TeamDamageType.ALL_DAMAGE)
        );

        int index = 3;
        for(Pair<String, TeamDamageType> damagePairs : damageTypes) {
            String damgeDisplay = damagePairs.getKey();
            TeamDamageType damageType = damagePairs.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + damgeDisplay)
                            .build(),
                    e -> {
                        serverable.announce("&6Friendly Fire: &e" + damgeDisplay);
                        serverable.getGameSettings().getTeamProvider().setTeamDamageType(damageType);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

