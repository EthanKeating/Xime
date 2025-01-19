package club.mcgamer.xime.loot;

import club.mcgamer.xime.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class LootTable {

    public abstract List<ItemStack> getTier1Items();
    public abstract List<ItemStack> getTier2Items();
    public abstract List<ItemStack> getSelectionChestItems();

    public abstract int getMaxItemCount();
    public static Random random = new Random();

    public List<ItemStack> getAllItems() {
        ArrayList<ItemStack> items = new ArrayList<>();

        items.addAll(getTier1Items());
        items.addAll(getTier2Items());
        items.addAll(getSelectionChestItems());

        return items;
    }

    public ItemStack getRandomItem() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.addAll(getTier1Items());
        items.addAll(getTier2Items());

        return items.get(random.nextInt(items.size() - 1));
    }
    public void fill(Chest chest, Tier tier, LootStyle lootStyle) {
        Random random = new Random();

        Inventory inventory = chest.getBlockInventory();

        ArrayList<Pair<ItemStack, Integer>> items;

        switch (lootStyle) {
            case INVERTED:
                items = generate(new ArrayList<>(tier == Tier.TWO ? getTier1Items() : getTier2Items()));
                break;
            case MIXED_ITEMS:
                items = generate(combineLists(getTier2Items(), getTier1Items()));
                break;
            case MIXED_CHESTS:
                items = generate(new ArrayList<>(random.nextBoolean() ? getTier1Items() : getTier2Items()));
                break;
            case ONLY_TIER_1:
                items = generate(new ArrayList<>(getTier1Items()));
                break;
            case ONLY_TIER_2:
                items = generate(new ArrayList<>(getTier2Items()));
                break;
            case DEFAULT:
            default:
                items = generate(new ArrayList<>(tier == Tier.ONE ? getTier1Items() : getTier2Items()));
                break;
        }

        for (Pair<ItemStack, Integer> loopItem : items)
            inventory.setItem(loopItem.getValue(), loopItem.getKey());

        chest.update();
    }

    private ArrayList<Pair<ItemStack, Integer>> generate(ArrayList<ItemStack> cloneTable) {
        ArrayList<Pair<ItemStack, Integer>> selectedItems = new ArrayList<>();

        int itemCount = getMaxItemCount() - (random.nextInt(2));

        for(int i = 0; i < itemCount; i++) {
            ItemStack selectedItem = cloneTable.get(random.nextInt(cloneTable.size()));
            cloneTable.remove(selectedItem);
            int selectedSlot = random.nextInt(27);
            selectedItems.add(new Pair<>(selectedItem, selectedSlot));
        }
        return selectedItems;
    }
    public enum Tier {
        ONE,
        TWO
    }

    public String toString() {
        return getClass()
                .getSimpleName()
                .replace("LootTable", "");
    }

    private static ArrayList<ItemStack> combineLists(List<ItemStack> list1, List<ItemStack> list2) {
        ArrayList<ItemStack> combinedList = new ArrayList<>();
        HashSet<String> seenMaterials = new HashSet<>();

        for (ItemStack item : list1) {
            if (item != null && !seenMaterials.contains(item.getType().toString())) {
                combinedList.add(item);
                seenMaterials.add(item.getType().toString());
            }
        }

        for (ItemStack item : list2) {
            if (item != null && !seenMaterials.contains(item.getType().toString())) {
                combinedList.add(item);
                seenMaterials.add(item.getType().toString());
            }
        }

        return combinedList;
    }
}

