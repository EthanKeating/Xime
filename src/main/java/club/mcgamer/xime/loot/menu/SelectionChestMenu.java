package club.mcgamer.xime.loot.menu;

import club.mcgamer.xime.fastinv.FastInv;
import club.mcgamer.xime.loot.tables.BadlionLootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class SelectionChestMenu extends FastInv {

    private boolean preventClose = true;

    public SelectionChestMenu(List<ItemStack> selectionLoot, Location dropLocation) {
        super(9 + ((selectionLoot.size() / 9) * 9), BadlionLootTable.SELECTION_CHEST_NAME);
        setCloseFilter(p -> this.preventClose);

        int index = 0;
        for(ItemStack loopItem : selectionLoot) {

            setItem(index++, loopItem, e -> {
                dropLocation.getWorld().dropItem(dropLocation.add(0.0, 0.5, 0.0), loopItem).setVelocity(new Vector(0.0, 0.0, 0.0));
                dropLocation.getWorld().playSound(dropLocation, Sound.ITEM_BREAK, 2, 2);
                dropLocation.getWorld().getBlockAt(dropLocation).setType(Material.AIR);
                preventClose = false;
                e.getWhoClicked().closeInventory();
            });
        }

    }



}
