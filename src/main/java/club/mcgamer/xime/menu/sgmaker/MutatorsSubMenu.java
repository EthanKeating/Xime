package club.mcgamer.xime.menu.sgmaker;

import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.sg.SGServerable;
import club.mcgamer.xime.util.Pair;
import club.mcgamer.xime.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MutatorsSubMenu extends FastInv {

    public MutatorsSubMenu(Profile profile, Profile sponsored, SGServerable serverable) {
        super(18, TextUtil.translate("Mutators"));


        setItem(2, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .name("&bStart Time")
                .lore("&aAdjust the time of day which the games starts.")
                .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(3, new ItemBuilder(Material.WATCH)
                        .name("&bGame Length")
                        .lore("&aAdjust the maximum length of a game.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(4, new ItemBuilder(Material.REDSTONE_LAMP_ON)
                        .name("&bDaylight Cycle")
                        .lore("&aIf disabled, the sun will not move.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(5, new ItemBuilder(Material.GOLDEN_APPLE)
                        .name("&bNatural Regeneration")
                        .lore("&aControls regeneration by satiation.", "&aIf disabled, you will need to eat golden", "&aapples to regenerate health.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(6, new ItemBuilder(Material.IRON_SWORD)
                        .name("&bGrace Period")
                        .lore("&aAdd a period at the beginning of the game", "&awhere combat is disabled.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(7, new ItemBuilder(Material.GLASS_BOTTLE)
                        .name("&bMax Health")
                        .lore("&aRaise or lower the max health of players.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(8, new ItemBuilder(Material.CHEST)
                        .name("&bChest Tiering")
                        .lore("&aRandomize, invert, or force all chest tiers.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });


        setItem(11, new ItemBuilder(Material.MOB_SPAWNER)
                        .name("&bDeathmatch Threshold")
                        .lore("&aChange the player count", "&athat the deathmatch starts at.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(12, new ItemBuilder(Material.NAME_TAG)
                        .name("&bSponsor Toggle")
                        .lore("&aEnable / Disable sponsor feature.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(13, new ItemBuilder(Material.SKULL_ITEM)
                        .name("&bBounty Toggle")
                        .lore("&aEnable / Disable bounty feature.")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(14, new ItemBuilder(Material.GOLD_NUGGET)
                        .name("&bPoints Gain / Lose")
                        .lore("&aEnable / Disable point gain / loss")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });

        setItem(14, new ItemBuilder(Material.GOLD_NUGGET)
                        .name("&bPoints Gain / Lose")
                        .lore("&aEnable / Disable point gain / loss")
                        .build(),
                e -> {
                    //OPEN START TIME SUB MENU
                });
    }
}

