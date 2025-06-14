package club.mcgamer.xime.menu.sg;

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

public class SponsorMenu extends FastInv {

    public SponsorMenu(Profile profile, Profile sponsored, SGServerable serverable) {
        super(9, TextUtil.translate("Sponsor: " + sponsored.getDisplayName()));

        String prefix = serverable.getPrefix();

        int index = 0;
        for(Pair<ItemStack, AtomicInteger> sponsorItem : new CopyOnWriteArrayList<>(serverable.getSponsorItems())) {
            int cost = sponsorItem.getValue().get();

            ItemStack originalItem = sponsorItem.getKey();
            ItemStack displayItem = sponsorItem.getValue().get() == -1 ? new ItemStack(Material.AIR)
                    : new ItemBuilder(originalItem.getType())
                    .amount(originalItem.getAmount())
                    .lore(String.format("&8[&6Cost&8] &e%s &apoints", cost))
                    .build();

            setItem(index++, displayItem, e -> {

                Player clicked = ((Player) e.getWhoClicked());

                if (sponsorItem.getValue().get() == -1) {
                    profile.sendMessage(prefix + "&cThat item is no longer available&8.");
                    return;
                }

                PlayerData playerData = profile.getPlayerData();
                if (playerData.getSgPoints() < cost) {
                    profile.sendMessage(prefix + "&4You do not have enough points&8.");
                    return;
                }
                if (!serverable.getTributeList().contains(sponsored)) {
                    profile.sendMessage(prefix + "&cYou cannot sponsor that player!");
                    return;
                }

                sponsorItem.getValue().set(-1);

                playerData.setSgPoints(playerData.getSgPoints() - cost);

                sponsored.getPlayer().getInventory().addItem(originalItem);
                sponsored.getPlayer().playSound(sponsored.getPlayer().getLocation(), Sound.LEVEL_UP, 2, 2);
                sponsored.sendMessage(prefix + "&fSurprise - you were sponsored by " + profile.getDisplayName());
                sponsored.sendMessage(prefix + "&fRemember to thank them!");
                profile.sendMessage(prefix + "&fItem sent to " + sponsored.getDisplayName());

                clicked.closeInventory();
                e.setCancelled(true);
            });
        }
    }


}
