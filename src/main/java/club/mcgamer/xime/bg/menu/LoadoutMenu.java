package club.mcgamer.xime.bg.menu;

import club.mcgamer.xime.bg.BGServerable;
import club.mcgamer.xime.data.entities.PlayerData;
import club.mcgamer.xime.fastinv.ItemBuilder;
import club.mcgamer.xime.profile.Profile;
import club.mcgamer.xime.util.IListener;
import club.mcgamer.xime.util.TextUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class LoadoutMenu extends IListener {

    private final BGServerable serverable;

    public void open(Profile profile) {
        if (profile.getServerable() != serverable) return;

        Inventory hotbarMenu = Bukkit.createInventory(null, 9, TextUtil.translate("&eHotbar Editor"));

        PlayerData playerData = profile.getPlayerData();

        hotbarMenu.setItem(playerData.getBgSwordSlot(), new ItemBuilder(Material.IRON_SWORD).name("&eSword Slot").build());
        hotbarMenu.setItem(playerData.getBgRodSlot(), new ItemBuilder(Material.FISHING_ROD).name("&eRod Slot").build());
        hotbarMenu.setItem(playerData.getBgBowSlot(), new ItemBuilder(Material.BOW).name("&eBow Slot").build());
        hotbarMenu.setItem(playerData.getBgFNSSlot(), new ItemBuilder(Material.FLINT_AND_STEEL).name("&eFlint & Steel Slot").build());
        hotbarMenu.setItem(playerData.getBgGapSlot(), new ItemBuilder(Material.GOLDEN_APPLE).name("&eGolden Apple Slot").build());
        hotbarMenu.setItem(playerData.getBgArrowSlot(), new ItemBuilder(Material.ARROW).name("&eArrow Slot").build());

        profile.getPlayer().openInventory(hotbarMenu);
    }

    @EventHandler
    private void onMenuClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals(TextUtil.translate("&eHotbar Editor")))
            return;

        Player player = (Player) event.getWhoClicked();
        Profile profile = plugin.getProfileHandler().getProfile(player);

        if (profile.getServerable() != serverable) return;

        if (event.getClick() != ClickType.RIGHT && event.getClick() != ClickType.LEFT) {
            event.setCancelled(true);
            return;
        }

        if (event.getSlotType() != InventoryType.SlotType.CONTAINER || event.getSlot() < 0 && event.getSlot() > 8)
            event.setCancelled(true);
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(TextUtil.translate("&eHotbar Editor")))
            return;

        Profile profile = plugin.getProfileHandler().getProfile((Player) event.getPlayer());
        PlayerData playerData = profile.getPlayerData();

        if (profile.getServerable() != serverable) return;

        int[] slots = new int[6];
        for(int i = 0; i < 9; i++) {
            if (event.getInventory().getItem(i) == null) continue;
            switch (event.getInventory().getItem(i).getType()) {
                case IRON_SWORD -> slots[0] = i;
                case FISHING_ROD -> slots[1] = i;
                case BOW -> slots[2] = i;
                case FLINT_AND_STEEL -> slots[3] = i;
                case GOLDEN_APPLE -> slots[4] = i;
                case ARROW -> slots[5] = i;
            }
        }
        for (int slot : slots) {
            if (slot > 8 || slot < 0) {
                profile.sendMessage(serverable.getPrefix() + "&cUnable to save loadout, missing an item.");
                return;
            }
        }
        playerData.setBgSwordSlot(slots[0]);
        playerData.setBgRodSlot(slots[1]);
        playerData.setBgBowSlot(slots[2]);
        playerData.setBgFNSSlot(slots[3]);
        playerData.setBgGapSlot(slots[4]);
        playerData.setBgArrowSlot(slots[5]);
        profile.sendMessage(serverable.getPrefix() + "&6Your loadout has been saved!");
    }

}