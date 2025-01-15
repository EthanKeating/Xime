package club.mcgamer.xime.loot;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class LootTable {

    public abstract List<ItemStack> getTier1Items();
    public abstract List<ItemStack> getTier2Items();
    public abstract List<ItemStack> getSelectionChestItems();

    public abstract int getAverageItemCount();
    public static Random random = new Random();

    public ItemStack getRandomItem() {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.addAll(getTier1Items());
        items.addAll(getTier2Items());

        return items.get(random.nextInt(items.size() - 1));
    }
    public void fill(Chest chest, Tier tier, LootStyle lootStyle) {
        Random random = new Random();

        Inventory inventory = chest.getBlockInventory();

        Set<ItemStack> items;

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

        List<Integer> allSlots = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            allSlots.add(i);
            allSlots.add(i);
        }

        Collections.shuffle(allSlots, random);

        int index = 0;
        for (ItemStack loopItem : items) {
            inventory.setItem(allSlots.get(index++), loopItem);
        }

        chest.update();
    }

    private Set<ItemStack> generate(ArrayList<ItemStack> cloneTable) {
        Set<ItemStack> selectedTable = new HashSet<>();
        Set<Material> selectedItems = new HashSet<>();
        Random random = new Random();

        int itemCount = getAverageItemCount() + random.nextInt(3) - 1;

        while(selectedItems.size() < itemCount) {

            ItemStack randomItem = cloneTable.get(random.nextInt(cloneTable.size()));
            if (selectedItems.contains(randomItem.getType()))
                continue;

            selectedTable.add(randomItem);
            selectedItems.add(randomItem.getType());
        }

        return selectedTable;
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

