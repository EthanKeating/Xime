package club.mcgamer.xime.loot.tables;

import club.mcgamer.xime.loot.LootTable;
import club.mcgamer.xime.util.ItemBuilder;
import club.mcgamer.xime.util.TextUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class MCSGLootTable extends LootTable {

    @Override
    public int getAverageItemCount() {
        return 5;
    }
    private final List<ItemStack> tier1Items = new ArrayList<ItemStack>(){{
        add(new ItemStack(Material.STONE_SWORD));
        add(new ItemStack(Material.WOOD_SWORD));
        add(new ItemStack(Material.WOOD_SWORD));
        add(new ItemStack(Material.STONE_AXE));
        add(new ItemStack(Material.WOOD_AXE));
        add(new ItemStack(Material.WOOD_AXE));

        add(new ItemBuilder(Material.LEATHER_HELMET).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_HELMET).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_CHESTPLATE).lore(Collections.singletonList(TextUtil.translate("&9+3 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_CHESTPLATE).lore(Collections.singletonList(TextUtil.translate("&9+3 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_LEGGINGS).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_LEGGINGS).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_BOOTS).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());
        add(new ItemBuilder(Material.LEATHER_BOOTS).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());

        add(new ItemStack(Material.BOW));
        add(new ItemStack(Material.FISHING_ROD));
        add(new ItemStack(Material.ARROW, 2));

        add(new ItemStack(Material.IRON_INGOT));
        add(new ItemStack(Material.GOLD_INGOT));
        add(new ItemStack(Material.STICK));
        add(new ItemStack(Material.STICK));
        //add(new ItemStack(Material.FEATHER, 5));
        add(new ItemStack(Material.FEATHER, 5));
        add(new ItemStack(Material.FLINT, 1));
//        add(new ItemStack(Material.FLINT, 1));

        add(new ItemStack(Material.BOWL));
        add(new ItemStack(Material.PUMPKIN_PIE));
        add(new ItemStack(Material.BAKED_POTATO));
        add(new ItemStack(Material.APPLE));
        add(new ItemStack(Material.COOKIE, 2));
        add(new ItemStack(Material.CARROT_ITEM));
        add(new ItemStack(Material.RAW_BEEF));
        add(new ItemStack(Material.RAW_CHICKEN));
        add(new ItemStack(Material.PORK));
        add(new ItemStack(Material.BREAD));
        add(new ItemStack(Material.RAW_FISH));

    }};

    private final List<ItemStack> tier2Items = new ArrayList<ItemStack>(){{
        add(new ItemStack(Material.STONE_SWORD));
        add(new ItemStack(Material.STONE_SWORD));

        add(new ItemBuilder(Material.IRON_HELMET).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());
        add(new ItemBuilder(Material.IRON_CHESTPLATE).lore(Collections.singletonList(TextUtil.translate("&9+6 Armor"))).build());
        add(new ItemBuilder(Material.IRON_LEGGINGS).lore(Collections.singletonList(TextUtil.translate("&9+5 Armor"))).build());
        add(new ItemBuilder(Material.IRON_BOOTS).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());

        add(new ItemBuilder(Material.CHAINMAIL_HELMET).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());
        add(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).lore(Collections.singletonList(TextUtil.translate("&9+5 Armor"))).build());
        add(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).lore(Collections.singletonList(TextUtil.translate("&9+4 Armor"))).build());
        add(new ItemBuilder(Material.CHAINMAIL_BOOTS).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());

        add(new ItemBuilder(Material.GOLD_HELMET).lore(Collections.singletonList(TextUtil.translate("&9+2 Armor"))).build());
        add(new ItemBuilder(Material.GOLD_CHESTPLATE).lore(Collections.singletonList(TextUtil.translate("&9+5 Armor"))).build());
        add(new ItemBuilder(Material.GOLD_LEGGINGS).lore(Collections.singletonList(TextUtil.translate("&9+3 Armor"))).build());
        add(new ItemBuilder(Material.GOLD_BOOTS).lore(Collections.singletonList(TextUtil.translate("&9+1 Armor"))).build());

        add(new ItemStack(Material.BOW));
        add(new ItemStack(Material.ARROW, 5));

        add(new ItemStack(Material.STICK));
        add(new ItemStack(Material.IRON_INGOT));
        add(new ItemStack(Material.GOLD_INGOT));
        add(new ItemStack(Material.DIAMOND));
        add(new ItemStack(Material.BOAT));
        add(new ItemStack(Material.FLINT_AND_STEEL));

        add(new ItemStack(Material.BAKED_POTATO));
        add(new ItemStack(Material.APPLE, 2));
        add(new ItemStack(Material.PUMPKIN_PIE, 2));
        add(new ItemStack(Material.GOLDEN_APPLE));
        add(new ItemStack(Material.GOLDEN_CARROT));
        add(new ItemStack(Material.BREAD));
        add(new ItemStack(Material.COOKED_BEEF));
        add(new ItemStack(Material.COOKED_CHICKEN));
        add(new ItemStack(Material.GRILLED_PORK));
        add(new ItemStack(Material.MUSHROOM_SOUP));
    }};

    @Override
    public List<ItemStack> getSelectionChestItems() {
        return new ArrayList<>();
    }
}
