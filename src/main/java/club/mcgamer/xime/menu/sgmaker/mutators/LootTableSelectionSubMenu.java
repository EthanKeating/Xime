package club.mcgamer.xime.menu.sgmaker.mutators;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.loot.tables.*;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class LootTableSelectionSubMenu extends FastInv {

    public LootTableSelectionSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(9, TextUtil.translate("Loot Table"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        List<Pair<String, LootTable>> lootTables = Arrays.asList(
                new Pair<>("MCSG", new MCSGLootTable()),
                new Pair<>("Badlion", new BadlionLootTable()),
                new Pair<>("Hive", new HiveLootTable()),
                new Pair<>("Clan Wars", new CWLootTable()),
                new Pair<>("SGHQ", new SGHQLootTable())
        );

        int index = 2;
        for(Pair<String, LootTable> lootTablePair : lootTables) {
            String lootTableName = lootTablePair.getKey();
            LootTable lootTable = lootTablePair.getValue();

            setItem(index++, new ItemBuilder(Material.WOOL)
                            .data(9)
                            .amount(1)
                            .name("&b" + lootTableName)
                            .build(),
                    e -> {
                        serverable.announce("&6Loot Table: &e" + lootTableName);
                        serverable.getGameSettings().setLootTable(lootTable);
                        previousMenu.open(profile.getPlayer());
                    });
        }
    }
}

