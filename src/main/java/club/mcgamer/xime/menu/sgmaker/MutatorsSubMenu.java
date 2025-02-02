package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.menu.sgmaker.mutators.*;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sgmaker.SGMakerServerable;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;

public class MutatorsSubMenu extends FastInv {

    public MutatorsSubMenu(FastInv previousMenu, Profile profile, SGMakerServerable serverable) {
        super(18, TextUtil.translate("Mutators"));

        setItem(0, new ItemBuilder(Material.WOOL)
                        .data(1)
                        .name("&aBack")
                        .build(),
                e -> {
                    previousMenu.open(profile.getPlayer());
                });

        setItem(2, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                        .name("&bPre Game Length")
                        .lore("&aAdjust the length of a pre game sequence.")
                        .build(),
                e -> {
                    new PreGameLengthSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(3, new ItemBuilder(Material.WATCH)
                        .name("&bGame Length")
                        .lore("&aAdjust the maximum length of a game.")
                        .build(),
                e -> {
                    new GameLengthSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });
        setItem(4, new ItemBuilder(Material.IRON_SWORD)
                        .name("&bGrace Period")
                        .lore("&aAdd a period at the beginning of the game", "&awhere combat is disabled.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                    profile.sendMessage("&cThis feature is still being worked on");
                    e.setCancelled(true);
                });

        setItem(5, new ItemBuilder(Material.MOB_SPAWNER)
                        .name("&bDeathmatch Threshold")
                        .lore("&aChange the player count", "&athat the deathmatch starts at.")
                        .build(),
                e -> {
                    new DeathmatchThresholdSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(6, new ItemBuilder(Material.DAYLIGHT_DETECTOR)
                        .name("&bDaylight Cycle")
                        .lore("&aIf disabled, the sun will not move.")
                        .build(),
                e -> {
                    new DaylightCycleSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(7, new ItemBuilder(Material.GOLDEN_APPLE)
                        .name("&bNatural Regeneration")
                        .lore("&aControls regeneration by satiation.", "&aIf disabled, you will need to eat golden", "&aapples to regenerate health.")
                        .build(),
                e -> {
                    new NaturalRegenerationSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(8, new ItemBuilder(Material.GLASS_BOTTLE)
                        .name("&bMax Health")
                        .lore("&aRaise or lower the max health of players.")
                        .build(),
                e -> {
                    new MaxHealSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(11, new ItemBuilder(Material.CHEST)
                        .name("&bLoot Style")
                        .lore("&aRandomize, invert, or force all chest tiers.")
                        .build(),
                e -> {
                    new LootStyleSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(12, new ItemBuilder(Material.ENDER_CHEST)
                        .name("&bLoot Table")
                        .lore("&aModify the loot table to match another server.")
                        .build(),
                e -> {
                    new LootTableSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(13, new ItemBuilder(Material.NAME_TAG)
                        .name("&bRandomize Names")
                        .lore("&aAdjust if everyone gets a random name.")
                        .build(),
                e -> {
                    new RandomizeNamesSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(14, new ItemBuilder(Material.YELLOW_FLOWER)
                        .name("&bItemized Flowers")
                        .lore("&aEnable / disable breaking flowers to get items.")
                        .build(),
                e -> {
                    new ItemizedFlowersSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });
        setItem(15, new ItemBuilder(Material.DIAMOND_AXE)
                        .name("&bNo Hit Delay")
                        .lore("&aEnable / disable no hit delay.")
                        .build(),
                e -> {
                    new NoHitDelaySelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(16, new ItemBuilder(Material.FISHING_ROD)
                        .name("&bRod Speed")
                        .lore("&aControl the speed of fishing rods")
                        .build(),
                e -> {
                    new RodSpeedSelectionSubMenu(this, profile, serverable).open(profile.getPlayer());
                });

        setItem(17, new ItemBuilder(Material.DIAMOND_BOOTS)
                        .name("&bKnockback")
                        .lore("&aModify the knockback to match another server.")
                        .build(),
                e -> {
                    profile.sendMessage("&cThis feature is still being worked on");
                    e.setCancelled(true);
                });

    }
}

